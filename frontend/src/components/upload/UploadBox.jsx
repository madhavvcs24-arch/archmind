import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";

function UploadBox() {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleAnalyze = async () => {
    if (!file) {
        alert("Please choose a ZIP file.");
        return;
    }

    setLoading(true);

    try {

        const uploadData = new FormData();
        uploadData.append("file", file);

        const analysis = await api.post("/upload", uploadData);

        const diagramData = new FormData();
        diagramData.append("file", file);

        const diagram = await api.post("/diagram", diagramData);

        const qualityData = new FormData();
        qualityData.append("file", file);

        const quality = await api.post("/quality", qualityData);

        navigate("/result", {
        state: {
            analysis: analysis.data,
            diagram: diagram.data,
            quality: quality.data,
        },
        });

    }catch (err) {
      console.error("FULL ERROR:", err);

      if (err.response) {
        console.log("Status:", err.response.status);
        console.log("Data:", err.response.data);
      } else if (err.request) {
        console.log("No response received");
      } else {
        console.log("Message:", err.message);
      }

      alert("Analysis failed.");
    }
    finally {
        setLoading(false);
    }
    };

  return (
    <div className="card shadow p-4">

      <h3 className="mb-4">
        Upload Java Project
      </h3>

      <input
        type="file"
        accept=".zip"
        className="form-control mb-3"
        onChange={(e) => setFile(e.target.files[0])}
      />

      <button
        className="btn btn-primary w-100"
        onClick={handleAnalyze}
        disabled={loading}
      >
        {loading ? "Analyzing..." : "Analyze Project"}
      </button>

    </div>
  );
}

export default UploadBox;