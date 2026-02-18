// 1. Handle File Selection
 function handleFileSelect() {
     const file = document.getElementById('fileInput').files[0];
     if (file) {
         document.getElementById('fileName').innerText = file.name;
         document.getElementById('dropZone').classList.add('active');
         document.getElementById('resultSection').style.display = 'none';
         document.getElementById('gapAnalysisSection').style.display = 'none';
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

     // UI: Show Loading
     const analyzeBtn = document.getElementById('analyzeBtn');
     const loadingOverlay = document.getElementById('loadingOverlay');
     const loadingText = document.getElementById('loadingText');

     analyzeBtn.disabled = true;
     loadingOverlay.classList.add('active');

     // Loading Animation Text Cycle
     const messages = [
         "Extracting text from PDF...",
         "Identifying ESG keywords...",
         "Checking BRSR compliance...",
         "Running AI scoring model...",
         "Finalizing results..."
     ];
     let msgIndex = 0;
     loadingText.innerText = messages[0];
     const messageInterval = setInterval(() => {
         msgIndex = (msgIndex + 1) % messages.length;
         loadingText.innerText = messages[msgIndex];
     }, 1500);

     try {
         const token = localStorage.getItem("token");
         const response = await fetch('http://localhost:8080/api/ai/analyze-file', {
             method: 'POST',
             headers: { 'Authorization': 'Bearer ' + token },
             body: formData
         });

         clearInterval(messageInterval);

         const text = await response.text();

         if (response.ok) {
             const data = JSON.parse(text);

             // --- A. Show Results ---
             document.getElementById('resultSection').style.display = 'block';

             // --- B. Update Scores ---
             document.getElementById('totalScore').innerText = Number(data.esg_score).toFixed(2);
             document.getElementById('eScore').innerText = Number(data.breakdown.E).toFixed(2);
             document.getElementById('sScore').innerText = Number(data.breakdown.S).toFixed(2);
             document.getElementById('gScore').innerText = Number(data.breakdown.G).toFixed(2);

             // --- Save Scores ---
             const scoresToPublish = {
                 e: data.breakdown.E,
                 s: data.breakdown.S,
                 g: data.breakdown.G,
                 avg: data.esg_score,
                 timestamp: new Date().getTime()
             };
             localStorage.setItem("latestGeneratedScores", JSON.stringify(scoresToPublish));

             // --- C. Analysis Text ---
             document.getElementById('aiAnalysisText').innerHTML = `
                 <strong style="color: #10b981; font-size: 15px;">Confidence: ${data.confidence || "High"}</strong>
                 <br>
                 <span style="display:block; margin-top:8px;">
                     ${data.analysis_summary || "Analysis complete."}
                 </span>
             `;

             // --- D. Render Gap Analysis ---
             if (data.gap_analysis) {
                 renderGaps(data.gap_analysis);
                 document.getElementById('gapAnalysisSection').style.display = 'block';
             }

             document.getElementById('fileName').innerText = "Analysis Complete ✓";

         } else {
             console.error("Server Error:", text);
             document.getElementById('fileName').innerText = "Upload Failed";
             try {
                 const errObj = JSON.parse(text);
                 alert("Analysis Failed: " + (errObj.error || errObj.message));
             } catch (e) {
                 alert("Server Error. Check console.");
             }
         }
     } catch (error) {
         clearInterval(messageInterval);
         console.error("Fetch error:", error);
         document.getElementById('fileName').innerText = "Connection Error";
         alert("Connection Error. Is backend running?");
     } finally {
         loadingOverlay.classList.remove('active');
         analyzeBtn.disabled = false;
     }
 }

 // 3. Render Gap Grid
 function renderGaps(gaps) {
     const container = document.getElementById("gapGrid");
     if(!container) return;
     container.innerHTML = "";

     const pillars = [
         { key: "Environmental", color: "#10b981", label: "Environment", class: "env" },
         { key: "Social", color: "#3b82f6", label: "Social", class: "soc" },
         { key: "Governance", color: "#f59e0b", label: "Governance", class: "gov" }
     ];

     pillars.forEach(p => {
         const info = gaps[p.key];
         if (!info) return;

         let contentHtml = "";

         if (!info.missing || info.missing.length === 0) {
             contentHtml = `<div class="all-good"><span>✓</span> All mandatory disclosures found.</div>`;
         } else {
             contentHtml = `<ul class="missing-list">`;
             info.missing.forEach(missingItem => {
                 const formattedItem = missingItem.item.charAt(0).toUpperCase() + missingItem.item.slice(1);

                 contentHtml += `
                     <li class="missing-item">
                         <span class="missing-keyword">${formattedItem}</span>
                         <span class="missing-suggestion">${missingItem.suggestion}</span>
                     </li>
                 `;
             });
             contentHtml += `</ul>`;
         }

         const html = `
             <div class="gap-card ${p.class}">
                 <div class="gap-header">
                     <span class="gap-pillar-name" style="color:${p.color}">${p.label}</span>
                     <span class="gap-score-badge" style="background:${p.color}20; color:${p.color}">
                         ${info.score}% Disclosed
                     </span>
                 </div>
                 ${contentHtml}
             </div>
         `;
         container.innerHTML += html;
     });
 }