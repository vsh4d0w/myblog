package com.lzq.myblog.service;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.IndentedCodeBlock;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Markdown 服务
 * 提供 Markdown 文本解析和渲染功能
 */
@Service
public class MarkdownService {
    
    private final Parser parser;
    private final HtmlRenderer renderer;
    
    public MarkdownService() {
        // 配置 CommonMark 扩展
        List<Extension> extensions = Arrays.asList(
                TablesExtension.create(),           // GFM 表格支持
                StrikethroughExtension.create(),    // 删除线 ~~text~~
                AutolinkExtension.create(),         // 自动链接
                TaskListItemsExtension.create()     // 任务列表 - [ ] / - [x]
        );
        
        // 创建解析器
        this.parser = Parser.builder()
                .extensions(extensions)
                .build();
        
        // 创建 HTML 渲染器，添加代码块语言类名支持
        this.renderer = HtmlRenderer.builder()
                .extensions(extensions)
                .softbreak("<br />")  // 软换行转为 <br>
                .attributeProviderFactory(context -> new CodeBlockAttributeProvider())
                .build();
    }
    
    /**
     * 代码块属性提供器，为代码块添加语言类名
     */
    private static class CodeBlockAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            if (node instanceof FencedCodeBlock) {
                FencedCodeBlock codeBlock = (FencedCodeBlock) node;
                String language = codeBlock.getInfo();
                if (language != null && !language.isEmpty()) {
                    // 只取语言名称的第一部分（去除可能的参数）
                    String lang = language.split("\\s+")[0].toLowerCase();
                    if ("code".equals(tagName)) {
                        attributes.put("class", "language-" + lang);
                    }
                }
            } else if (node instanceof IndentedCodeBlock) {
                if ("code".equals(tagName)) {
                    attributes.put("class", "language-plaintext");
                }
            }
        }
    }
    
    /**
     * 将 Markdown 文本渲染为 HTML
     * @param markdown Markdown 原文
     * @return 渲染后的 HTML
     */
    public String renderToHtml(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }
        
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
    
    /**
     * 从 Markdown 内容中提取纯文本摘要
     * @param markdown Markdown 原文
     * @param maxLength 最大长度
     * @return 纯文本摘要
     */
    public String extractSummary(String markdown, int maxLength) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }
        
        // 移除 Markdown 标记，提取纯文本
        String plainText = markdown
                // 移除代码块
                .replaceAll("```[\\s\\S]*?```", "")
                // 移除行内代码
                .replaceAll("`[^`]+`", "")
                // 移除图片
                .replaceAll("!\\[[^\\]]*\\]\\([^)]*\\)", "")
                // 移除链接，保留文字
                .replaceAll("\\[([^\\]]+)\\]\\([^)]*\\)", "$1")
                // 移除标题标记
                .replaceAll("^#{1,6}\\s+", "")
                // 移除粗体/斜体
                .replaceAll("\\*{1,2}([^*]+)\\*{1,2}", "$1")
                .replaceAll("_{1,2}([^_]+)_{1,2}", "$1")
                // 移除删除线
                .replaceAll("~~([^~]+)~~", "$1")
                // 移除列表标记
                .replaceAll("^[\\-*+]\\s+", "")
                .replaceAll("^\\d+\\.\\s+", "")
                // 移除引用标记
                .replaceAll("^>\\s+", "")
                // 移除水平线
                .replaceAll("^[-*_]{3,}$", "")
                // 合并多个空白字符
                .replaceAll("\\s+", " ")
                .trim();
        
        // 截取指定长度
        if (plainText.length() <= maxLength) {
            return plainText;
        }
        return plainText.substring(0, maxLength) + "...";
    }
}
