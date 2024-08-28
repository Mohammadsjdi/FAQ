package com.example.faqgenerator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FAQController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/")
    public String processFAQ(@RequestParam("faq_input") String faqInput, Model model) {
        // در اینجا محدودیت IP را حذف کرده‌ایم
        String processedOutput = processInput(faqInput);
        model.addAttribute("faq_input", faqInput);
        model.addAttribute("processed_output", processedOutput);
        return "index";
    }

    private String processInput(String input) {
        String[] lines = input.split("\n");
        StringBuilder output = new StringBuilder();
        
        if (lines.length < 2) {
            return "Error faq";
        }

        String bf = "";
        String no = "";
        String[] tar = {"زمستان", "پاییز", "تابستان", "ارزان", "لوکس", "ترکیبی", "لحظه آخری"};
        String[] lstg = {"اهواز", "دبی", "کیش", "مشهد", "شیراز", "اصفهان", "تبریز"};
        String[] mn = lines[0].split(" ");

        for (String ig : mn) {
            for (String city : lstg) {
                if (ig.equals(city)) {
                    bf += ig;
                }
            }
        }

        for (String j : mn) {
            for (String season : tar) {
                if (j.equals(season)) {
                    no += "تور " + j;
                }
            }
        }

        if (no.isEmpty()) {
            no = "تور";
        }
        
        output.append("<div class=\"container mb--3\">");
        output.append(" <div class=\"row\">");
        output.append("  <div class=\"c-accordion w-100\">");
        output.append("   <div class=\"accordion\">");
        output.append("    <h2 class=\"accordion__main-title\">سوالات کاربران درباره خرید ").append(no).append(" ").append(bf).append("</h2>");
        output.append("    <div class=\"accordion__list\">");

        for (int n = 0; n < lines.length - 1; n += 2) {
            if (lines[n].trim().isEmpty()) continue;
            output.append("     <div class=\"accordion__item\">");
            output.append("      <div class=\"accordion__header js-accordion\"><h3>").append(lines[n]).append("</h3> <i class=\"icon-arrow-down\"></i></div>");
            output.append("        <div class=\"accordion__content\">");
            output.append("         <p>").append(lines[n + 1]).append("</p>");
            output.append("        </div>");
            output.append("       </div>");
        }

        output.append("    </div>");
        output.append("   </div>");
        output.append("  </div>");
        output.append(" </div>");
        output.append("</div>");

        output.append("<script type=\"application/ld+json\">");
        output.append("{");
        output.append("  \"@context\": \"https://schema.org\",");
        output.append("  \"@type\": \"FAQPage\",");
        output.append("  \"mainEntity\": [");

        for (int n = 0; n < lines.length - 1; n += 2) {
            if (lines[n].trim().isEmpty()) continue;
            output.append("    {");
            output.append("      \"@type\": \"Question\",");
            output.append("      \"name\": \"").append(lines[n]).append("\",");
            output.append("      \"acceptedAnswer\": {");
            output.append("        \"@type\": \"Answer\",");
            output.append("        \"text\": \"").append(lines[n + 1]).append("\"");
            output.append("      }");
            output.append("    }");
            if (n + 2 < lines.length) {
                output.append(",");
            }
        }

        output.append("  ]");
        output.append("}");
        output.append("</script>");

        return output.toString();
    }
}
