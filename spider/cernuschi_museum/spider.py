import urllib.request
from lxml import etree
import csv
import time
real_url=[]
Dynasty_number=[]
title_summary=[]
author_summary=[]
img_url_summary=[]
introduction_summary=[]
img_not_find_list=[8,15,16,25,38,39,43,44,46,57,72,77,96,103,104,113,117,175]
url_list=["https://www.cernuschi.paris.fr/zh-hans/collections/zhong-guo-guan-cang/hong-shan-wen-hua",
          "https://www.cernuschi.paris.fr/en/collections/chinese-collections/shang-period",
          "https://www.cernuschi.paris.fr/zh-hans/collections/zhong-guo-guan-cang/zhou-zhao",
          "https://www.cernuschi.paris.fr/zh-hans/collections/zhong-guo-guan-cang/yi-zhao",
          "https://www.cernuschi.paris.fr/zh-hans/collections/zhong-guo-guan-cang/liu-zhao-shi-qi",
          "https://www.cernuschi.paris.fr/zh-hans/collections/zhong-guo-guan-cang/sui-tang",
          "https://www.cernuschi.paris.fr/zh-hans/collections/zhong-guo-guan-cang/liao-zhao",
          "https://www.cernuschi.paris.fr/zh-hans/collections/zhong-guo-guan-cang/song-yuan-shi-qi",
          "https://www.cernuschi.paris.fr/zh-hans/collections/zhong-guo-guan-cang/ming-zhao",
          "https://www.cernuschi.paris.fr/zh-hans/collections/zhong-guo-guan-cang/qing-zhao",
          "https://www.cernuschi.paris.fr/zh-hans/collections/zhong-guo-guan-cang/xian-dang-dai-zhong-guo-zi-1911nian-qi"
          ]
dict={1:"红山文化",2:"商",3:"周",4:"汉",5:"六朝",6:"隋唐",7:"辽",8:"宋元",9:"明",10:"清",11:"现当代中国"}
headers={
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7',
    'Accept-Language': 'zh-CN,zh;q=0.9',
    'Cache-Control': 'max-age=0',
    'Cookie': 'orejime={"strictly_necessary":1,"tracking":false}; SERVERID=vm06',
    'Referer': 'https://www.cernuschi.paris.fr/zh-hans/collections/zhong-guo-guan-cang/yi-zhao',
    'Sec-Ch-Ua': '"Chromium";v="122", "Not(A:Brand";v="24", "Google Chrome";v="122"',
    'Sec-Ch-Ua-Mobile': '?0',
    'Sec-Ch-Ua-Platform': '"Windows"',
    'Sec-Fetch-Dest': 'document',
    'Sec-Fetch-Mode': 'navigate',
    'Sec-Fetch-Site': 'same-origin',
    'Sec-Fetch-User': '?1',
    'Upgrade-Insecure-Requests': '1',
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.6261.95 Safari/537.36',
}

def create_request(url):
    request=urllib.request.Request(url=url,headers=headers)
    return request

def get_content(request):
    response=urllib.request.urlopen(request)
    content=response.read().decode('utf-8')
    return content

def get_content1(request,count):
    response=urllib.request.urlopen(request)
    content=response.read().decode('utf-8')
    return content

def get_element(content,num):
    tree=etree.HTML(content)
    url_list1=tree.xpath('//div[@class="col-sm-6 col-md-3"]/a/@href')
    for url in url_list1:
        url="https://www.cernuschi.paris.fr"+url
        real_url.append(url)
        Dynasty_number.append(num)
        
def get_element1(content):
    tree=etree.HTML(content)
    title=tree.xpath('//div[@class="col-sm-7 col-md-5 col-md-offset-1"]/h1/text()')[0]
    title_summary.append(title)
    #print(title)
    
    author_list=tree.xpath('//div[@class="col-sm-7 col-md-5 col-md-offset-1"]/p[@class="author"]/text()')
    if author_list:
        author=author_list[0]
    else:
        author="未找到"
    author_summary.append(author)
    #print(author)
    
    img_url=tree.xpath('//img/@src')[1]
    img_url_summary.append(img_url)
    #print(img_url)
    
    introduction=tree.xpath('//p/text()')[2][:500]
    introduction_summary.append(introduction)
    #print(introduction)
        


def save_data():
    fp = open('data15.csv', 'w',encoding='utf-8')
    data = csv.writer(fp)
    row = ('Id', 'name', 'author', 'relicTime', 'imageUrl', 'description','realUrl')
    data.writerow(row)
    for pos in range(len(title_summary)):
        if pos+1 in img_not_find_list:    
            relicTime=dict[Dynasty_number[pos]]
            row = (pos+1, title_summary[pos], author_summary[pos],relicTime, 
                "未找到", introduction_summary[pos],real_url[pos])
        else:
            relicTime=dict[Dynasty_number[pos]]
            row = (pos+1, title_summary[pos], author_summary[pos],relicTime, 
                img_url_summary[pos], introduction_summary[pos],real_url[pos])
        data.writerow(row)

def down_load():
    for i in range(len(img_url_summary)):
        if i+1==64:
            urllib.request.urlretrieve(url=url,filename='./cernuschi_museum_image/'+str(i+1)+'_'+"Buddha dit"+'.jpg')
        elif i+1==131:
            urllib.request.urlretrieve(url=url,filename='./cernuschi_museum_image/'+str(i+1)+'_'+"Saisir la victoire dans tout le pays – le président Mao avec ses cadres"+'.jpg')
        elif i+1==142:
            urllib.request.urlretrieve(url=url,filename='./cernuschi_museum_image/'+str(i+1)+'_'+"Encre orageuse"+'.jpg')
        elif i+1==159:
            urllib.request.urlretrieve(url=url,filename='./cernuschi_museum_image/'+str(i+1)+'_'+"Untitled"+'.jpg')
        elif i+1 in img_not_find_list:
            continue
        else:
            name=title_summary[i]
            url=img_url_summary[i]
            urllib.request.urlretrieve(url=url,filename='./cernuschi_museum_image/'+str(i+1)+'_'+name+'.jpg')    
    
    
if __name__ =='__main__':
    num=0
    for url in url_list:
        num=num+1
        request=create_request(url)
        content=get_content(request)
        get_element(content,num)
        time.sleep(2)
        #print(len(Dynasty_number))
    print("part1 finished")
    count=0
    for url in real_url:
        count=count+1
        request=create_request(url)
        content=get_content1(request,count)
        get_element1(content)
        print(count)
        time.sleep(2)
    print("part2 finished")
    save_data()
    print("part3 finished")
    down_load()
    print("part4 finished")
    print("finished!!!")
    
   
    

