Array.prototype.each = Array.prototype.forEach;

if (!Array.prototype.find) {
    Array.prototype.find = function(predicate) {
        if (this == null) {
            throw new TypeError('Array.prototype.find called on null or undefined');
        }
        if (typeof predicate !== 'function') {
            throw new TypeError('predicate must be a function');
        }
        var list = Object(this);
        var length = list.length >>> 0;
        var thisArg = arguments[1];
        var value;

        for (var i = 0; i < length; i++) {
            value = list[i];
            if (predicate.call(thisArg, value, i, list)) {
                return value;
            }
        }
        return undefined;
    };
}

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

function http(request) {
    var handler = new XMLHttpRequest();
    handler.open(request.method, request.url, true);
    return function(responseHandler) {
        handler.addEventListener("readystatechange", function () {
            if (handler.readyState == 4) {
                responseHandler({status: handler.status, entity: handler.responseText });
            }
        });
        handler.send(request.entity);
    };
}

document.addEventListener('DOMContentLoaded', function () {
    select('code.language-url').each(function(element) {
        code.highlight(element, [
            {pattern: "[:/]", cssClass: "keyword" }
        ]);
    });

    select('code.language-xml').each(function(element) {
        code.highlight(element, [
            {pattern: '^\\w', cssClass: "quote" },
            {pattern: "&lt;[^\\s&]+", cssClass: "keyword" },
            {pattern: "\\??&gt;", cssClass: "keyword" },
            {pattern: "\\s[\\w:-]+=", cssClass: "constant" }
        ]);
    });

    select('code.language-java').each(function(element) {
        code.highlight(element, [
            {pattern: '"[^"]*"', cssClass: "quote" },
            {pattern: "(?:public|void|class|import|static|true|false|throws|[,.;])", cssClass: "keyword" },
            {pattern: "\\d+", cssClass: "literal" },
            {pattern: "//.*$", cssClass: "comment" },
            {pattern: "\\w+\\b(?![\\(\\.])", cssClass: "keyword" },
        ]);
    });

    select('code.language-generic').each(function(element) {
        code.highlight(element, [
            {pattern: "'[^']*'", cssClass: "quote" },
            {pattern: '"[^"]*"', cssClass: "quote" },
            {pattern: "[.,:;{}<=+%]|&lt;", cssClass: "keyword" }
        ]);
    });

    http({method: "GET", url: "https://d39xxm2detz5wm.cloudfront.net/repos/bodar/totallylazy/releases" })(function(response) {
        if(response.status != 200) return;
        var release = JSON.parse(response.entity).find(function (release) {
            return release.prerelease == false
        });
        select(".latest").each(function(element) {
            element.innerHTML = element.innerHTML.
                replace("${LATEST_VERSION}", release.tag_name).
                replace("Latest", release.tag_name).
                replace("Release notes", release.body);
        });
    });
});