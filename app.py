from flask import Flask, render_template, request

app = Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'POST':
        faq = request.form['faq_input']
        
        m = faq.split("\n")
        processed_output = "Error faq"
        
        if len(m) < 2:
            return render_template('index.html', faq_input=faq, processed_output=processed_output)
        elif len(m) > 1:   
            bf = ""
            no = ""
            tar = ["زمستان", "پاییز", "تابستان", "ارزان", "لوکس", "ترکیبی", "لحظه آخری"]
            mn = m[0].split(" ")
            lstg = ['اهواز', 'دبی', 'کیش', 'مشهد', 'شیراز', 'اصفهان', 'تبریز']  # ادامه لیست شهرها

            for ig in mn:
                if ig in lstg:
                    bf += ig
            for j in mn:
                if j in tar:
                    no += "تور " + j

            if no == "":
                no = "تور"
            n = 0

            tor = bf

            output = []

            output.append('<div class="container mb--3">')
            output.append(' <div class="row">')
            output.append('  <div class="c-accordion w-100">')
            output.append('   <div class="accordion">')
            output.append('    <h2 class="accordion__main-title">' + "سوالات کاربران درباره خرید " + no + " " + tor + '</h2>')
            output.append('    <div class="accordion__list">')

            while n < len(m) - 1:
                if m[n].strip() == "":
                    n += 1
                    continue
                output.append('     <div class="accordion__item">')
                output.append('      <div class="accordion__header js-accordion"><h3>' + m[n] + '</h3> <i class="icon-arrow-down"></i></div>')
                output.append('        <div class="accordion__content">')
                output.append('         <p>' + m[n + 1] + '</p>')
                output.append('        </div>')
                output.append('       </div>')
                n += 2

            output.append('    </div>')
            output.append('   </div>')
            output.append('  </div>')
            output.append(' </div>')
            output.append('</div>')

            n = 0
            output.append('<script type="application/ld+json">// <![CDATA[')
            output.append('{')
            output.append('  "@context": "https://schema.org",')
            output.append('  "@type": "FAQPage",')
            output.append('  "mainEntity": [')

            while n < len(m) - 1:
                if m[n].strip() == "":
                    n += 1
                    continue
                output.append('    {')
                output.append('      "@type": "Question",')
                output.append('      "name": "' + m[n] + '",')
                output.append('      "acceptedAnswer": {')
                output.append('        "@type": "Answer",')
                output.append('        "text": "' + m[n + 1] + '"')
                output.append('      }')
                output.append('    }')
                if n + 2 < len(m):
                    output.append(',')
                n += 2

            output.append('  ]')
            output.append("}")
            output.append("// ]]></script>")
            output.append('<p></p>')

            processed_output = "\n".join(output)
            return render_template('index.html', faq_input=faq, processed_output=processed_output)

    return render_template('index.html')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
