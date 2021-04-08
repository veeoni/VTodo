//初始化markdown编辑器
var contentEditor;
$(function() {
    contentEditor = editormd.markdownToHTML("content", {
        // width   : "90%",
        // height  : 640,
        // syncScrolling : "single",
        // path    : "../static/lib/editormd/lib/",
        htmlDecode      : "style,script,iframe",  // you can filter tags decode
        toc             : true,
        tocm            : true,    // Using [TOCM]
        // tocContainer    : "#custom-toc-container", // 自定义 ToC 容器层
        gfm             : true,
        // tocDropdown     : true,
        markdownSourceCode : true, // 是否保留 Markdown 源码，即是否删除保存源码的 Textarea 标签
        emoji           : true,
        taskList        : true,
        tex             : true,  // 默认不解析
        flowChart       : true,  // 默认不解析
        sequenceDiagram : true,  // 默认不解析
    });
});