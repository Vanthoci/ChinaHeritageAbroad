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
button1=driver.find_element(By.XPATH,'//*[@id="app"]/div[2]/div[3]/div[1]/div[2]/div/div/div[2]')
# inner_button=driver.find_element(By.XPATH,'//*[@id="app"]/div[2]/div[3]/div[2]/div/div[2]/div[4]/div[2]/div/span[2]/div/input')
# inner_button.clear()
# inner_button.send_keys(873)
# button.click()
button1.click()
button=driver.find_element(By.CLASS_NAME,'btn-next')

for i in range(2):
    page=page+1
    timestamp = driver.execute_script("return window.performance.timing.loadEventEnd;")/1000
    _url="https://www.lnmuseum.com.cn/singleMuseum/bs/bsDigitalRelics/list?_t="+str(timestamp)+"&year=&category=&name=&currentPage="+str(page)+"&size=20&three=1"
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
    p_list=content_dict['result']
    data_list=p_list['records']
    dict_list=[]
    for dict in data_list:
        list_dict={}
        list_dict['collectName']=dict['collectName']
        list_dict['collectYear']=dict['collectYear']
        list_dict['collectDetailYear']=dict['collectDetailYear']
        list_dict['collectType']=dict['collectType']
        list_dict['collectLevel']=dict['collectLevel']
        list_dict['collectTexture']=dict['collectTexture']
        list_dict['detailSize']=dict['detailSize']
        list_dict['unearthedLocation']=dict['unearthedLocation']
        list_dict['collectSynopsis']=dict['collectSynopsis']
        list_dict['threeDimensionalInfo']="https://www.lnmuseum.com.cn"+dict['threeDimensionalInfo']
        image=[]
        for data in dict['collectImages']:
            image.append("https://www.lnmuseum.com.cn"+data['attPath'])
        list_dict['collectImages']=image
        dict_list.append(list_dict)
    csv_file='3D_LiaoNing_museum.csv'
    fieldnames = list(dict_list[0].keys())
    with open(csv_file, mode='a', newline='', encoding='utf-8') as file:
        writer = csv.DictWriter(file, fieldnames=fieldnames)
        # 写入列名
        if flag==0:
            writer.writeheader()
            flag=1
        # 写入数据
        for list_data in dict_list:
            writer.writerow(list_data)
    time.sleep(2)
    button.click()
input()


