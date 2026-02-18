import sys
import os

class SilenceLogs:
    def __enter__(self):
        self._original_stdout = sys.stdout
        self._original_stderr = sys.stderr
        sys.stdout = open(os.devnull, 'w')
        sys.stderr = open(os.devnull, 'w')

    def __exit__(self, exc_type, exc_val, exc_tb):
        sys.stdout.close()
        sys.stderr.close()
        sys.stdout = self._original_stdout
        sys.stderr = self._original_stderr

with SilenceLogs():
    import json
    import warnings
    import torch
    import numpy as np
    import pdfplumber
    from transformers import AutoTokenizer, AutoModelForSequenceClassification
    import logging

    warnings.filterwarnings("ignore")
    logging.getLogger("transformers").setLevel(logging.ERROR)

class ESGInference:
    def __init__(self, model_path):
        if not os.path.exists(model_path):
            raise FileNotFoundError(f"Model not found at {model_path}")

        self.tokenizer = AutoTokenizer.from_pretrained(model_path)
        self.model = AutoModelForSequenceClassification.from_pretrained(model_path)
        self.model.eval()

        self.checklist = {
            "Environmental": [
                {"keyword": "scope 1", "context_clues": ["emissions", "ghg", "carbon footprint"]},
                {"keyword": "scope 2", "context_clues": ["emissions", "electricity", "purchased energy"]},
                {"keyword": "water consumption", "context_clues": ["water management", "water withdrawal", "natural resources"]},
                {"keyword": "waste management", "context_clues": ["circular economy", "recycling", "hazardous waste"]},
                {"keyword": "renewable energy", "context_clues": ["energy consumption", "solar", "wind power"]},
                {"keyword": "energy intensity", "context_clues": ["energy consumption", "efficiency", "megajoules"]},
            ],
            "Social": [
                {"keyword": "training hours", "context_clues": ["employee development", "skill upgradation", "training programs"]},
                {"keyword": "safety incidents", "context_clues": ["health and safety", "lost time injury", "ltifr"]},
                {"keyword": "gender ratio", "context_clues": ["diversity", "inclusion", "workforce"]},
                {"keyword": "minimum wages", "context_clues": ["remuneration", "fair wages", "compensation"]},
                {"keyword": "maternity benefits", "context_clues": ["parental leave", "employee welfare", "benefits"]},
                {"keyword": "human rights", "context_clues": ["policy", "due diligence", "stakeholder engagement"]},
                {"keyword": "posh", "context_clues": ["sexual harassment", "prevention", "internal complaints committee"]},
            ],
            "Governance": [
                {"keyword": "anti-corruption", "context_clues": ["bribery", "ethics", "code of conduct"]},
                {"keyword": "whistleblower", "context_clues": ["vigil mechanism", "speak up policy", "grievance redressal"]},
                {"keyword": "board diversity", "context_clues": ["board of directors", "composition", "independence"]},
                {"keyword": "related party transactions", "context_clues": ["rpt policy", "audit committee", "conflicts of interest"]},
            ]
        }

    def extract_text(self, pdf_path):
        text_chunks, full_text_lower = [], ""
        try:
            with pdfplumber.open(pdf_path) as pdf:
                for page in pdf.pages:
                    content = page.extract_text()
                    if content:
                        chunks = [p.strip() for p in content.split('\n') if len(p.strip()) > 40]
                        text_chunks.extend(chunks)
                        full_text_lower += content.lower() + " "
        except Exception:
            return [], ""
        return text_chunks, full_text_lower

    def calculate_robust_score(self, confidences, total_chunks):
        if not confidences or total_chunks == 0: return 0.0
        volume_factor = np.log1p(len(confidences))
        quality_factor = np.mean(confidences)
        density_factor = len(confidences) / total_chunks
        raw_score = (volume_factor * quality_factor) + (density_factor * 10)
        final_score = min(100, raw_score * 15)
        return round(final_score, 2)

    def perform_gap_analysis(self, full_text):
        gap_report = {}
        for pillar, items in self.checklist.items():
            found_count = 0
            missing_items_with_context = []

            for item in items:
                keyword = item["keyword"]
                if keyword in full_text:
                    found_count += 1
                else:
                    found_clues = [clue for clue in item["context_clues"] if clue in full_text]
                    suggestion = f"Try adding this near your discussion of '{', '.join(found_clues)}'." if found_clues else "This topic appears to be completely missing from the report."

                    missing_items_with_context.append({
                        "item": keyword,
                        "suggestion": suggestion
                    })

            percentage = (found_count / len(items)) * 100
            gap_report[pillar] = {
                "score": round(percentage, 1),
                "missing": missing_items_with_context
            }
        return gap_report

    def analyze(self, pdf_path):
        # 1. Extract Text
        chunks, full_text = self.extract_text(pdf_path)
        total_chunks = len(chunks)
        if total_chunks == 0: return None

        # 2. Run Gap Analysis (Checklist)
        gap_results = self.perform_gap_analysis(full_text)

        # 3. Run AI Scoring (Text Quality)
        results = {0: [], 1: [], 2: []}
        for chunk in chunks:
            inputs = self.tokenizer(chunk, return_tensors="pt", truncation=True, padding=True, max_length=128)
            with torch.no_grad():
                outputs = self.model(**inputs)

            probs = torch.nn.functional.softmax(outputs.logits, dim=-1)
            conf, pred = torch.max(probs, dim=1)

            if conf.item() > 0.80:
                results[pred.item()].append(conf.item())

        # 4. Calculate Raw AI Scores
        raw_scores = {
            "E": self.calculate_robust_score(results[0], total_chunks),
            "S": self.calculate_robust_score(results[1], total_chunks),
            "G": self.calculate_robust_score(results[2], total_chunks)
        }

        # 5. MERGE SCORES (Hybrid Logic)
        # Formula: 70% AI Quality + 30% Checklist Compliance
        # This penalizes high AI scores if mandatory items are missing.

        final_scores = {}
        key_map = {"E": "Environmental", "S": "Social", "G": "Governance"}

        for short_key, long_key in key_map.items():
            ai_val = raw_scores[short_key]
            gap_val = gap_results[long_key]["score"]

            # Hybrid Calculation
            hybrid_val = (ai_val * 0.7) + (gap_val * 0.3)
            final_scores[short_key] = round(hybrid_val, 2)

        # Overall Average
        avg_score = round(np.mean(list(final_scores.values())), 2)

        return {
            "esg_score": avg_score,
            "breakdown": final_scores, # Sending the Hybrid scores to frontend
            "gap_analysis": gap_results,
            "analysis_summary": f"Hybrid Analysis: Score weighted 70% on Content Quality (AI) and 30% on BRSR Compliance.",
            "confidence": "High"
        }

if __name__ == "__main__":
    try:
        if len(sys.argv) < 2:
            print(json.dumps({"error": "No file path provided"}))
        pdf_path = sys.argv[1]
        script_dir = os.path.dirname(os.path.abspath(__file__))
        model_dir = os.path.join(script_dir, "models", "final_esg_model")
        with SilenceLogs():
            scorer = ESGInference(model_dir)
            result = scorer.analyze(pdf_path)
        if result:
            print(json.dumps(result))
        else:
            print(json.dumps({"error": "Could not extract text from PDF"}))
    except Exception as e:
        print(json.dumps({"error": str(e)}))