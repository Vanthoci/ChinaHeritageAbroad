
from django.shortcuts import render
from django.views.decorators.http import require_http_methods
from openai import OpenAI
import httpx
import pandas as pd
import json
import re

from django.shortcuts import redirect

def redirect_to_llm(request):
    return redirect('connect_LLM')  


collection_data = pd.read_excel('unit.xlsx')


@require_http_methods(["GET", "POST"])
def connect_LLM(request):
    if 'message_history' not in request.session:
        request.session['message_history'] = []
    
    if request.method == "POST":
        if 'action' in request.POST and request.POST['action'] == 'update_question':
            request.session['message_history'] = []
        else:
            question = request.POST.get('question', '')
            request.session['message_history'].append({'role': 'user', 'content': question})
            answer = 'No answer found.'
            request.session['message_history'].append({'role': 'assistant', 'content': answer})
            except httpx.HTTPError as http_err:
                answer = f"An HTTP error occurred: {http_err}"
                request.session['message_history'].append({'role': 'assistant', 'content': answer})
            except Exception as e:
                answer = f"An unexpected error occurred: {str(e)}"
                request.session['message_history'].append({'role': 'assistant', 'content': answer})

            request.session.modified = True

    
    message_history = request.session.get('message_history', [])
    
    return render(request, 'qa_page.html', {'message_history': message_history})
