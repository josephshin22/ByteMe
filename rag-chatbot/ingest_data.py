from langchain_community.document_loaders import PyPDFDirectoryLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_openai.embeddings import OpenAIEmbeddings
from langchain_chroma import Chroma
from uuid import uuid4
import os

# Directly set the API key (Replace "your-api-key" with your actual OpenAI API key)
os.environ["OPENAI_API_KEY"] = "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"

# Configuration
DATA_PATH = r"data"
CHROMA_PATH = r"chroma_db"

# Initiate the embeddings model
embeddings_model = OpenAIEmbeddings(model="text-embedding-3-large")

# Initiate the vector store
vector_store = Chroma(
    collection_name="example_collection",
    embedding_function=embeddings_model,
    persist_directory=CHROMA_PATH,
)

# Load the PDF documents
loader = PyPDFDirectoryLoader(DATA_PATH)
raw_documents = loader.load()

# Split the documents into chunks
text_splitter = RecursiveCharacterTextSplitter(
    chunk_size=300,
    chunk_overlap=100,
    length_function=len,
    is_separator_regex=False,
)
chunks = text_splitter.split_documents(raw_documents)

# Generate unique IDs for each chunk
uuids = [str(uuid4()) for _ in chunks]

# Add chunks to the vector store
vector_store.add_documents(documents=chunks, ids=uuids)
