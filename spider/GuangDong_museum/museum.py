import requests
from lxml import etree
import csv

headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36 Edg/123.0.0.0"
}

with open('data.csv', mode='a', encoding='utf-8', newline='') as file:
    for j in range(1, 120):
        url = f"https://www.gdmuseum.com/cn/col49/list_{j}"

        response = requests.get(url, headers=headers)

        data = etree.HTML(response.text)

        lis = data.xpath('/html/body/div[7]/div/div[2]/ul/li')

        writer = csv.writer(file)

        if j == 1:
            writer.writerow(['名称', '年代', '类型', '图片', '详情'])  # 写入表头
        for i in lis:
            t = i.xpath('./a/p[1]/span[2]/text()')[0]  # 名称

            t1 = i.xpath('./a/p[2]/span[2]/text()')  # 年代
            if len(t1) == 0:
                t11 = ""
            else:
                t11 = t1[0]

            t2 = i.xpath('./a/p[3]/span[2]/text()')  # 类型
            if len(t2) == 0:
                t22 = ""
            else:
                t22 = t2[0]

            t3 = i.xpath('./a/div/div/img/@src')  # 图片
            if len(t3) == 0:
                t33 = ""
            else:
                t33 = 'https://www.gdmuseum.com' + i.xpath('./a/div/div/img/@src')[0]

            t4 = 'https://www.gdmuseum.com' + i.xpath('./a/@href')[0]  # 详情

            writer.writerow([t, t11, t22, t33, t4])
