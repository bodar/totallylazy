Array.prototype.each = Array.prototype.forEach;

RegExp.prototype.replace = function(str, replacer, nonMatchedReplacer) {
    nonMatchedReplacer = nonMatchedReplacer || function(value) {
        return value
    };
    var result = [];

    var position = 0;
    var match;
    while (( match = this.exec(str)) != null) {
        result.push(nonMatchedReplacer(str.substring(position, match.index)));
        result.push(replacer(match));
        position = this.lastIndex;
    }
    result.push(nonMatchedReplacer(str.substring(position)));

    return result.join("");
};

function select(selector, element){
    element = element || document;
    var nodes = element.querySelectorAll(selector);
    var array = [];
    for (var i = 0; i < nodes.length; ++i) {
        array[i] = nodes[i];
    }
    return array;
}

function code() {}

code.highlight = function(element, pairs) {
    if (pairs.length == 0) {
        return;
    }
    var classes = [];
    var matchGroups = [];
    pairs.each(function(p) {
        matchGroups.push("(", p.pattern, ")", "|");
        classes.push(p.cssClass);
    });
    matchGroups.pop();
    var regex = new RegExp(matchGroups.join(""), "gm");

    element.innerHTML = regex.replace(element.innerHTML, function(match) {
        var matches = match.slice(1);
        for (var i = 0; i < matches.length; i++) {
            if (matches[i]) {
                return '<span class="' + classes[i] + '">' + matches[i] + '</span>'
            }
        }
    });
};

document.addEventListener('DOMContentLoaded', function () {
    select('code.language-xml').each(function(element) {
        code.highlight(element, [
            {pattern: '"[^"]*"', cssClass: "quote" },
            {pattern: "&lt;[^\\s&]+", cssClass: "keyword" },
            {pattern: "\\??&gt;", cssClass: "keyword" },
            {pattern: "\\s[\\w:-]+=", cssClass: "constant" }
        ]);
    });

    select('code.language-java').each(function(element) {
        code.highlight(element, [
            {pattern: '"[^"]*"', cssClass: "quote" },
            {pattern: "(?:public|void|class|true|false|throws|[,.;])", cssClass: "keyword" },
            {pattern: "\\d+", cssClass: "literal" },
            {pattern: "//.*$", cssClass: "comment" },
            {pattern: "\\w+\\b(?!\\()", cssClass: "keyword" },
        ]);
    })
});



