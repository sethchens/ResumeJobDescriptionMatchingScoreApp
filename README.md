# Overview

This mobile app takes resume and job description in texts from the user and provide an insight of how closely these two texts are related. The purpose of this project is to integrate previous projects as a whole, as well as to understand the communication between Firebase, Flask, and mobile app. The outcome of this project fulfills some future works of previous projects, making the app more complete and solve a rea-life problem. 

[Software Demo Video](https://youtu.be/uEMt2ciIbrs)

# Cloud Database

The cloud database used in this project is Firebase database.
The structure of this database is simple. It consists of three fields: Resume_text, job_description_text, score. The first two fields comes directly from the user, and score is from the ML model that compute the semantic simialrity (where 0 represents no relation and 1 indicates a strong relation).

# Development Environment
## Tools
- Android Studio
- Flask
- Firebase
- HuggingFace Pipelines
- pyTorch
- numpy

## Language
- Kotlin
- Python

# Useful Websites

* [StatQuest(youtube channel)](https://www.youtube.com/watch?v=e9U0QAFbfLI)
* [betterExplained.com](https://betterexplained.com/articles/vector-calculus-understanding-the-dot-product/)
- [W3School](https://www.w3schools.com/KOTLIN/index.php)
- [Android Jetpack](https://developer.android.com/jetpack?gad_source=1&gclid=CjwKCAiAnpy9BhAkEiwA-P8N4hnIcwH3FUOnCwcNeQxYAyfwDH89t4Dns9peVMRXZ35wo0ktZFosexoC3t4QAvD_BwE&gclsrc=aw.ds)
- [ChatGPT]

# Future Work
- Modern UI response
- Deploy Flask server on Firebase
- App testing
