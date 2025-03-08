import tensorflow as tf
from transformers import TFMobileBertModel
import numpy as np
import tensorflow_text  # Needed for text preprocessing in TFLite

# Load the model with tf library
model = TFMobileBertModel.from_pretrained("google/mobilebert-uncased")

# convert it to TFLite
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]  # Optimize for mobile
tflite_model = converter.convert()

# Save the TFLite model
with open("model.tflite", "wb") as f:
    f.write(tflite_model)

# Load the TensorFlow Lite model, grap the input and get the output from the model
interpreter = tf.lite.Interpreter(model_path="model.tflite")
interpreter.allocate_tensors()












# Every model has a specific input shape and data type, 
# so getting input and output tensor details the model requires
# helps knowing what to pass in and to expect
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

def preprocess_text(texts, tokenizer):
    """Tokenize and preprocess input text."""
    tokenized = tokenizer(texts, return_tensors="tf", padding=True, truncation=True, max_length=512)
    return tokenized

def match_BERT_tflite(resume, job_description, tokenizer):
    # Tokenize inputs
    tokenized_resumes = preprocess_text(resume, tokenizer)
    tokenized_jobs = preprocess_text(job_description, tokenizer)

    '''The first param of set_tensor and get_tensor is to tell the interpreter
    to which slot would the input/output tensor go (since the model can have
    multiple input/ouput tensors)'''
    # Convert to numpy arrays and ensure they match the TFLite model's expected shape
    input_data_resumes = np.array(tokenized_resumes['input_ids'], dtype=np.int32)
    input_data_jobs = np.array(tokenized_jobs['input_ids'], dtype=np.int32)

    # Masks are to tell BERT which are real words and which are paddings
    mask_resumes = np.array(tokenized_resumes["attention_mask"], dtype=np.int32)
    mask_jobs = np.array(tokenized_jobs["attention_mask"], dtype=np.int32)

    # Check if token_type_ids are needed
    token_type_resumes = token_type_jobs = None
    if "token_type_ids" in tokenized_resumes:
        token_type_resumes = np.array(tokenized_resumes["token_type_ids"], dtype=np.int32)
        token_type_jobs = np.array(tokenized_jobs["token_type_ids"], dtype=np.int32)

    # Run inference for resumes
    interpreter.set_tensor(input_details[0]["index"], input_data_resumes)
    interpreter.set_tensor(input_details[1]["index"], mask_resumes)
    if token_type_resumes is not None:
        interpreter.set_tensor(input_details[2]["index"], token_type_resumes)
    interpreter.invoke()
    output_resumes = interpreter.get_tensor(output_details[0]["index"])

    # Run inference for job descriptions
    interpreter.set_tensor(input_details[0]["index"], input_data_jobs)
    interpreter.set_tensor(input_details[1]["index"], mask_jobs)
    if token_type_jobs is not None:
        interpreter.set_tensor(input_details[2]["index"], token_type_jobs)
    interpreter.invoke()
    output_jobs = interpreter.get_tensor(output_details[0]["index"])

    # Normalize embeddings so the magnitude effect is diminished wen calcualting
    # similarity
    normalized_resumes = output_resumes / np.linalg.norm(output_resumes, axis=1, keepdims=True)
    normalized_jobs = output_jobs / np.linalg.norm(output_jobs, axis=1, keepdims=True)

    # Compute cosine similarity
    similarity_matrix = np.dot(normalized_resumes, normalized_jobs.T)

    return similarity_matrix