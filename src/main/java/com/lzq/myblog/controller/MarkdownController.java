package com.lzq.myblog.controller;

import com.lzq.myblog.common.Result;
import com.lzq.myblog.dto.MarkdownRenderDTO;
import com.lzq.myblog.service.MarkdownService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Markdown 控制器
 * 提供 Markdown 渲染和预览功能
 */
@Tag(name = "Markdown工具", description = "Markdown 渲染和预览接口")
@RestController
@RequestMapping("/api/markdown")
@RequiredArgsConstructor
public class MarkdownController {
    
    private final MarkdownService markdownService;
    
    /**
     * 渲染 Markdown 为 HTML
     */
    @Operation(summary = "渲染Markdown", description = "将 Markdown 文本渲染为 HTML，支持 GFM 扩展（表格、删除线、任务列表等）")
    @PostMapping("/render")
    public Result<String> render(@Valid @RequestBody MarkdownRenderDTO renderDTO) {
        String html = markdownService.renderToHtml(renderDTO.getMarkdown());
        return Result.success(html);
    }
    
    /**
     * 从 Markdown 提取摘要
     */
    @Operation(summary = "提取摘要", description = "从 Markdown 内容中提取纯文本摘要")
    @PostMapping("/summary")
    public Result<String> extractSummary(@Valid @RequestBody MarkdownRenderDTO renderDTO) {
        int maxLength = renderDTO.getMaxLength() != null ? renderDTO.getMaxLength() : 200;
        String summary = markdownService.extractSummary(renderDTO.getMarkdown(), maxLength);
        return Result.success(summary);
    }
}
