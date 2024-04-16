from django.shortcuts import render, redirect
from django.http import HttpRequest, HttpResponse
def func(request):
    return HttpResponse("Hello world!")
def user_list(request):
    # 第一个位子是视图函数的request参数，第二个参数位是html文件路径
    return render(request, "eg.html")





def login(request: HttpRequest) -> HttpResponse:
    if request.method == 'GET':
        return render(request, "login.html")

    elif request.method == 'POST':
        username = request.POST.get("username")
        password = request.POST.get("password")

        if username == "admin" and password == "password123":
            return redirect("https://www.example.com/dashboard/")  # Redirect to dashboard
        else:
            return render(request, "login.html", {"error_message": "Invalid username or password!"})

    return render(request, "login.html")
