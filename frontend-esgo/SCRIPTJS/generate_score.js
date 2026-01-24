// 1. Handle File Selection Visuals
function handleFileSelect() {
    const file = document.getElementById('fileInput').files[0];
    if (file) {
        document.getElementById('fileName').innerText = file.name;

        // Visual feedback on the upload box
        const zone = document.querySelector('.upload-zone');
        if (zone) {
            zone.style.borderColor = "#2ecc71";
            zone.style.backgroundColor = "#f0fdf4";
        }
    }
}

// 2. Main Upload Logic
async function uploadAndScore() {
    const fileInput = document.getElementById('fileInput');
    if (fileInput.files.length === 0) {
        alert("Please select a file first.");
        return;
    }

    const formData = new FormData();
    formData.append("file", fileInput.files[0]);

    // UI: Show Loading State
    document.getElementById('fileName').innerText = "Gemini AI is analyzing... please wait...";
    const btn = document.querySelector('button[onclick="uploadAndScore()"]');
    if(btn) {
        btn.disabled = true;
        btn.innerText = "Processing...";
        btn.style.opacity = "0.7";
    }

    try {
        const token = localStorage.getItem("token");

        // Send to Java Backend -> which forwards to Python Gemini
        const response = await fetch('http://localhost:8080/api/ai/analyze-file', {
            method: 'POST',
            headers: { 'Authorization': 'Bearer ' + token },
            body: formData
        });

        // Read raw text first to handle errors safely
        const text = await response.text();

        if (response.ok) {
            const data = JSON.parse(text);

            // --- A. Show The Section ---
            document.getElementById('resultSection').style.display = 'block';

            // --- B. Update Scores ---
            document.getElementById('totalScore').innerText = data.esg_score;
            document.getElementById('eScore').innerText = data.breakdown.E;
            document.getElementById('sScore').innerText = data.breakdown.S;
            document.getElementById('gScore').innerText = data.breakdown.G;

            // --- C. Update Text with Gemini's Explanation ---
            // We use the new 'analysis_summary' field from the Python code
            const summary = data.analysis_summary || "Analysis complete.";
            const confidence = data.confidence || "High";

            document.getElementById('aiAnalysisText').innerHTML = `
                <strong style="color: #2ecc71; font-size: 14px;">AI Verdict: ${confidence}</strong>
                <br>
                <span style="display:block; margin-top:8px; font-style: normal; color: #333; line-height: 1.5;">
                    ${summary}
                </span>
            `;

            document.getElementById('fileName').innerText = "Analysis Complete ✓";
        } else {
            // Error Handling
            console.error("Server Error:", text);
            try {
                const errObj = JSON.parse(text);
                // Show the nice error message from Python (e.g. "Invalid Document")
                alert("Analysis Failed: " + (errObj.error || errObj.message));
            } catch (e) {
                alert("Server Error. Check console for details.");
            }
            document.getElementById('fileName').innerText = "Upload Failed";
        }
    } catch (error) {
        console.error(error);
        alert("Connection Error. Is the backend running?");
        document.getElementById('fileName').innerText = "Connection Error";
    } finally {
        // Reset Button
        if(btn) {
            btn.disabled = false;
            btn.innerText = "Analyze Report";
            btn.style.opacity = "1";
        }
    }
}