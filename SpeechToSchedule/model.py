'''
Run the following to get the model folder, then compress and upload it to Kaggle Notebook's input directory
'''

from transformers import AutoTokenizer, AutoModelForSeq2SeqLM

# # T5
tokenizer = AutoTokenizer.from_pretrained("google-t5/t5-base")
model = AutoModelForSeq2SeqLM.from_pretrained("google-t5/t5-base")

model.save_pretrained("./t5-base")
tokenizer.save_pretrained("././t5-base")

# facebook/bart-large-cnn
tokenizer_b = AutoTokenizer.from_pretrained("facebook/bart-base")
model_b = AutoModelForSeq2SeqLM.from_pretrained("facebook/bart-base")

model_b.save_pretrained("././bart-base")
tokenizer_b.save_pretrained("././bart-base")