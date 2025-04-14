from langchain_openai import ChatOpenAI, OpenAIEmbeddings
from langchain_chroma import Chroma
import gradio as gr
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import uvicorn
import os
import threading

# Directly set the API key (Replace "your-api-key" with your actual OpenAI API key)
import os
os.environ["OPENAI_API_KEY"] = "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"

# FastAPI app instance
app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    # Allow all origins (or specify your React app URL like ["http://localhost:3000"])
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],  # Allow all HTTP methods (GET, POST, etc.)
    allow_headers=["*"],  # Allow all headers
)

# Configuration
DATA_PATH = r"data"
CHROMA_PATH = r"chroma_db"

embeddings_model = OpenAIEmbeddings(model="text-embedding-3-large")

# Initiate the model
llm = ChatOpenAI(temperature=0.5, model='gpt-4o-mini')

# Connect to the ChromaDB
vector_store = Chroma(
    collection_name="example_collection",
    embedding_function=embeddings_model,
    persist_directory=CHROMA_PATH,
)

# Set up the vector store retriever
num_results = 5
retriever = vector_store.as_retriever(search_kwargs={'k': num_results})

# Function to generate responses


def stream_response(message, history):
    # Retrieve relevant chunks
    docs = retriever.invoke(message)

    # Aggregate retrieved knowledge
    knowledge = "\n\n".join(doc.page_content for doc in docs)

    # Construct the prompt for the LLM
    rag_prompt = f"""
    You are an assistant which answers questions based on provided knowledge.
    You do NOT use any internal knowledge but solely rely on "The knowledge" section.
    You do NOT mention that the information comes from the knowledge provided.

    The question: {message}

    Conversation history: {history}

    The knowledge: {knowledge}
    """

    # Stream response to Gradio
    partial_message = ""
    for response in llm.stream(rag_prompt):
        partial_message += response.content
        yield partial_message


# === Gradio App ===
chatbot = gr.ChatInterface(
    stream_response,
    textbox=gr.Textbox(
        placeholder="Ask your question...",
        container=False,
        autoscroll=True,
        scale=7
    ),
    title="RAG-powered Chatbot",
    theme="soft",
)

# === Run Gradio in a thread ===
def launch_gradio():
    chatbot.launch(server_name="0.0.0.0", server_port=7860, share=False)

gr_thread = threading.Thread(target=launch_gradio)
gr_thread.start()

# === Run FastAPI Server ===
if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
