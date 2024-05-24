# 导入必要的库
from django.shortcuts import render
from django.views.decorators.http import require_http_methods
from openai import OpenAI
import httpx
import pandas as pd
import json
import re
from django.http import JsonResponse 
import time
from django.shortcuts import redirect

def redirect_to_llm(request):
    return redirect('connect_LLM')  # 使用在 urls.py 中定义的 name 参数


# 从Excel文件加载收藏信息
collection_data = pd.read_excel('unit.xlsx')

from django.http import JsonResponse

@require_http_methods(["GET", "POST"])
def connect_LLM(request):
    if 'message_history' not in request.session:
        request.session['message_history'] = []
        return JsonResponse({'message_history': request.session['message_history']})

    if request.method == "GET":
        action = request.GET.get('action', '')
        print("S1")
        if action == 'load_histroy':
            print("S2")
            return JsonResponse({'message_history': request.session['message_history']})

    if request.method == "POST":
        data = json.loads(request.body)
        action = data.get('action', '')

        if action == 'clear_history':
            request.session['message_history'] = []
        elif action == 'update_question':
            question = data.get('question', '')
            request.session['message_history'].append({'role': 'user', 'content': question})
            request.session['message_history'].append({'role': 'assistant', 'content': "思考中...", 'flag': "flag"})
            request.session.modified = True
        else:
            # 从POST数据中提取用户的问题，并将其添加到会话的消息历史中
            question = data.get('question', '')
            # request.session['message_history'].append({'role': 'user', 'content': question})
            # 根据用户问题，在加载的收藏数据中搜索相关信息
            print(question + "_ myquestion")
            # 定义要处理的字符串
            # 使用正则表达式查找"的"或"位于"或"来自"或"是"或"所属"之前的所有字符
            match = re.search(r'^(.*?)(的|位于|来自|是|所属)', question)

            # 如果找到匹配，输出匹配的部分，否则输出整个字符串
            result = match.group(1) if match else question

            relevant_info = collection_data[collection_data['藏品'].str.contains(result, case=False, na=False)]
            context = "Here is what I found about the item: " + str(relevant_info.to_dict()) if not relevant_info.empty else "No specific information found."

            if not relevant_info.empty:
                print(relevant_info)
                info_str = json.dumps(relevant_info.to_dict(orient='records'), indent=4)
                guidance = "Remember that you are a museum Q&A assistant. Please provide a brief Chinese response based on the information I give you, "
                context = f"{guidance}Here is what I found about the item:\n{info_str}"
            else:
                context = "Remember that you are a museum Q&A assistant. Please provide a brief response in Chinese. Could you please provide more specifics?"

            # 配置客户端
            client = OpenAI(
                base_url="https://hk.xty.app/v1",
                api_key="sk-KuS6VKYhcbKqGPfnFfB2D7D8AbF8451f80177b8cC8CbDfAb",  # 请替换成你的OpenAI API密钥
                http_client=httpx.Client(
                    base_url="https://hk.xty.app/v1",
                    follow_redirects=True,
                    timeout=50.0  # 设置请求超时时间
                ),
            )

            print("OK HERE")
            # 创建API请求
            try:
                response = client.chat.completions.create(
                    model="gpt-3.5-turbo",
                    messages=[
                        {"role": "system", "content": context},
                        {"role": "user", "content": question}
                    ]
                )
                # 提取并处理响应
                if response.choices:
                    answer = response.choices[0].message.content
                else:
                    answer = 'No answer found.'
            except httpx.HTTPError as http_err:
                answer = f"An HTTP error occurred: {http_err}"
            except Exception as e:
                answer = f"An unexpected error occurred: {str(e)}"

            print(answer)
            if ('flag' in request.session['message_history'][-1]) :
                request.session['message_history'].pop()

            request.session['message_history'].append({'role': 'assistant', 'content': answer})
            request.session.modified = True

        return JsonResponse({'message_history': request.session['message_history']})
    
    message_history = request.session.get('message_history', [])
    return render(request, 'qa_page.html', {'message_history': message_history})
