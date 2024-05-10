import time
# import requests
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
import urllib.request
import json
import csv

timestamp=0
page=0
flag=0

service = Service(executable_path='E:\\vscode1\\python\\urllib_use\\chromedriver.exe')
options = webdriver.ChromeOptions()
options.add_experimental_option("detach",True)
options.add_argument('-ignore-certificate-errors')
options.add_argument('-ignore -ssl-errors')
options.add_argument('--disable-gpu')
driver=webdriver.Chrome(service=service,options=options)
url="https://www.lnmuseum.com.cn/#/collect/digital-culture"
driver.get(url)
button=driver.find_element(By.CLASS_NAME,"btn-next")
# inner_button=driver.find_element(By.XPATH,'//*[@id="app"]/div[2]/div[3]/div[2]/div/div[2]/div[4]/div[2]/div/span[2]/div/input')
# inner_button.clear()
# inner_button.send_keys(873)
# button.click()

for i in range(0,1747):
    page=page+1
    timestamp = driver.execute_script("return window.performance.timing.loadEventEnd;")/1000
    _url="https://www.lnmuseum.com.cn/singleMuseum/szwwkt/list?_t="+str(timestamp)+"&year=&category=&name=&currentPage="+str(page)+"&size=15"
    headers={
        "user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36"
    }
    request=urllib.request.Request(url=_url,headers=headers)
    response=urllib.request.urlopen(request)
    content=response.read().decode('utf-8')

    with open ('museum.json','w',encoding='utf-8') as fp:
        fp.write(content)
    with open('museum.json', 'r', encoding='utf-8') as fp:
        content = fp.read()
# 将JSON字符串转换为Python字典
    content_dict = json.loads(content)
    data_list=content_dict['result']
    csv_file='LiaoNing_museum.csv'
    fieldnames = list(data_list[0].keys())
    with open(csv_file, mode='a', newline='', encoding='utf-8') as file:
        writer = csv.DictWriter(file, fieldnames=fieldnames)
        # 写入列名
        if flag==0:
            writer.writeheader()
            flag=1
        # 写入数据
        for list_data in data_list:
            writer.writerow(list_data)
    time.sleep(2)
    button.click()
input()


