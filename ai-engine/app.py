from flask import Flask, request, jsonify
import google.generativeai as genai
import PyPDF2
import docx
import io
import json
import os

app = Flask(__name__)

# =========================================================
# 1. CONFIGURATION
# =========================================================

# PASTE YOUR API KEY HERE
GOOGLE_API_KEY = "AIzaSyCV5JsLr-j96Z5hGEZD_R-xoN0v0ZF0NjE"

genai.configure(api_key=GOOGLE_API_KEY)

# Use the standard model name.
# If this fails with 404, change it to 'gemini-pro'
model = genai.GenerativeModel('gemini-2.5-flash')

# =========================================================
# 2. TEXT EXTRACTION
# =========================================================

def extract_text_from_pdf(file_stream):
    try:
        reader = PyPDF2.PdfReader(file_stream)
        text = ""
        max_pages = min(len(reader.pages), 30)
        for i in range(max_pages):
            page_text = reader.pages[i].extract_text()
            if page_text: text += page_text + " "
        return text
    except Exception as e:
        print(f"PDF Error: {e}")
        return ""

def extract_text_from_docx(file_stream):
    try:
        doc = docx.Document(file_stream)
        text = ""
        for para in doc.paragraphs: text += para.text + " "
        for table in doc.tables:
            for row in table.rows:
                for cell in row.cells: text += cell.text + " "
        return text
    except Exception as e:
        print(f"DOCX Error: {e}")
        return ""

# =========================================================
# 3. THE PROMPT
# =========================================================

def get_esg_prompt(report_text):
    return f"""
    You are an expert ESG Auditor. Analyze the following Corporate Sustainability Report text.

    TASK:
    Assign a score (0-100) for Environmental (E), Social (S), and Governance (G).

    CRITERIA:
    - E: Net Zero targets, renewable energy, waste mgmt, ISO 14001.
    - S: Gender diversity (>15%), safety (LTIFR), human rights.
    - G: Transparency, committees, board independence.

    VALIDATION:
    - If this is NOT a valid ESG/Sustainability report, return "0" scores and set confidence to "Invalid Document".

    OUTPUT JSON ONLY:
    {{
        "esg_score": <Weighted Avg: E*0.4 + S*0.3 + G*0.3>,
        "breakdown": {{ "E": <0-100>, "S": <0-100>, "G": <0-100> }},
        "confidence": "<High/Medium/Low/Invalid>",
        "analysis_summary": "<2 sentence explanation of the score>"
    }}

    REPORT TEXT:
    {report_text[:100000]}
    """

# =========================================================
# 4. API ENDPOINT
# =========================================================

@app.route('/analyze-file', methods=['POST'])
def analyze_file():
    if 'file' not in request.files:
        return jsonify({"error": "No file uploaded"}), 400

    file = request.files['file']
    filename = file.filename.lower()
    print(f"Processing File: {filename}")

    try:
        # 1. Read File
        file_bytes = file.read()
        file_stream = io.BytesIO(file_bytes)

        # 2. Extract Text
        full_text = ""
        if filename.endswith('.pdf'):
            full_text = extract_text_from_pdf(file_stream)
        elif filename.endswith('.docx') or filename.endswith('.doc'):
            full_text = extract_text_from_docx(file_stream)
        else:
            return jsonify({"error": "Unsupported format"}), 400

        if len(full_text) < 200:
            return jsonify({"error": "File empty or unreadable"}), 400

        # 3. Ask Gemini
        print("Sending to Gemini...")
        prompt = get_esg_prompt(full_text)

        # Correct syntax for google-generativeai library
        response = model.generate_content(prompt)

        raw_json = response.text.strip()

        # Clean Markdown
        if raw_json.startswith("```json"): raw_json = raw_json[7:]
        if raw_json.startswith("```"): raw_json = raw_json[3:]
        if raw_json.endswith("```"): raw_json = raw_json[:-3]

        result_data = json.loads(raw_json)

        print(f"Result: {result_data}")
        return jsonify(result_data)

    except Exception as e:
        print(f"Error: {e}")
        return jsonify({"error": "AI Analysis Failed. Server Error."}), 500

if __name__ == '__main__':
    print("Gemini ESG Engine Running on Port 5000...")
    app.run(port=5000)