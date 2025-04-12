import { useParams } from "react-router-dom";

function Chatbot() {
    return (
        <div style={{ padding: "1rem" }}>
            <h1>Chat with our Assistant</h1>
            <iframe
                src="http://localhost:7860"
                title="Gradio Chatbot"
                width="100%"
                height="600px"
                style={{ border: "none", borderRadius: "12px" }}
            />
        </div>
    );
}

export default Chatbot;
