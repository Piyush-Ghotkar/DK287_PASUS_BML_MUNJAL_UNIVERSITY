import speech_recognition as sr #importing the library
r=sr.Recognizer()
with sr.Microphone() as source:
    print('Start speaking')
    audio = r.listen(source)
    #with open('speech.wav','wb') as f:
        #f.write(audio.get_wav_data())
    print('Done listening, Processing.....')
    print(audio)
    print(r.recognize_google(audio))
try:
    print('Text is:', r.recognize_google(audio))
    inp= r.recognize_google(audio)
except:
    print('error occured!')
    pass

