Array.prototype.each = Array.prototype.forEach;
Array.prototype.append = Array.prototype.push;

define(Array, "find", function (predicate) {
    for (var i = 0; i < this.length; i++) {
        if (predicate(this[i], i)) return this[i];
    }
    return undefined;
});

define(Array, "head", function () {
    return this[0];
});

define(Array, "exists", function(predicate) {
    return this.find(predicate) != undefined;
});


Function.identity = function(x){
    return x;
};

RegExp.escape = function(string){
    return string.replace(/([.*+?^${}()|\[\]\/\\])/g, "\\$1");
};

String.prototype.replaceAll = function(string, replacement){
  return this.replace(new RegExp(string, "g"), replacement);
};

function define(object, name, fun) {
    if (!object.prototype[name]) object.prototype[name] = fun;
}

define(String, "contains", function(value) {
   return this.indexOf(value) != -1;
});

define(String, "startsWith", function(value) {
   return this.indexOf(value) == 0;
});


function Selection() {}
Selection.prototype = [];
Selection.prototype.on = function(name, fun) {
    this.each(function(element) {
        element.addEventListener(name, fun);
    });
};
Selection.prototype.attribute = function(name, value) {
    if(value == null) return this.map(function(element) {
        return element.getAttribute(name);
    });
    return this.map(function(element) {
        return element.setAttribute(name, value);
    });
};
Selection.prototype.text = function(value) {
    if(value == null) return this.map(function(element) {
        return element.textContent;
    });
    return this.map(function(element) {
        return element.textContent = value;
    });
};
Selection.prototype.html = function(value) {
    if(value == null) return this.map(function(element) {
        return element.innerHTML;
    });
    return this.map(function(element) {
        return element.innerHTML = value;
    });
};


function select(selector, element) {
    element = element || document;
    var nodes = element.querySelectorAll(selector);
    var array = new Selection();
    for (var i = 0; i < nodes.length; ++i) {
        array.append(nodes[i]);
    }
    return array;
}

function head(selector, element){
    return select(selector, element).head();
}

function get(id) {
    return document.getElementById(id);
}

function http(request) {
    var handler = new XMLHttpRequest();
    handler.open(request.method, request.url, true);
    var headers = request.headers || {};
    for (var name in headers) {
        handler.setRequestHeader(name, headers[name]);
    }
    return function (responseHandler) {
        handler.addEventListener("readystatechange", function () {
            if (handler.readyState == 4) {
                var headers = handler.getAllResponseHeaders().split("\n").reduce(function(accumulator, header) {
                    var pair = header.split(": ");
                    accumulator[pair[0]] = pair[1];
                    return accumulator;
                }, {});
                var entity = {
                    toXml: function () {
                        return handler.responseXML;
                    },
                    toText: function () {
                        return handler.responseText;
                    }
                };
                responseHandler({status: handler.status, headers: headers, entity: entity});
            }
        });
        handler.send(request.entity);
        return function () {
            handler.abort();
        }
    };
}

function html(name, attributes, value){
    if(arguments.length == 2){
        value = attributes;
        attributes = {};
    } else if (arguments.length == 1) {
        value = '';
        attributes = {};
    }
    var e = document.createElement(name);
    for (var name in attributes) {
        e.setAttribute(name, attributes[name]);
    }
    e.innerHTML = value;
    return e;
}

function onload(handler) {
    document.addEventListener('DOMContentLoaded', handler);
}

function fire(element, name) {
    if ("createEvent" in document) {
        var event = document.createEvent("HTMLEvents");
        event.initEvent(name, false, true);
        element.dispatchEvent(event);
    }
    else element.fireEvent("on" + name);
}

function delay(timeout, fun) {
    var id = -1;
    return function () {
        if (id != -1) clearTimeout(id);
        id = setTimeout(fun, timeout);
    };
}

function sequenced(fun) {
    var sequence = 0;
    return function () {
        var current = ++sequence;
        var guard = function (async) {
            return function () {
                if (current < sequence) return;
                return async.apply(null, arguments);
            }
        };
        return fun(guard);
    }
}

function request(form){
    var inputs = select("input[name], textarea[name], select[name]", form);
    var message = inputs.filter(function(input){
        return !((input.type == "radio" || input.type == "checkbox") && !input.checked);
    }).map(function(input){
        return encodeURIComponent(input.name) + "=" + encodeURIComponent(input.value);
    }).join("&");
    var method = (form.getAttribute("method") || "GET").toUpperCase();
    var uri = form.getAttribute("action");
    var headers = {};
    var entity = "";
    if(method == "GET"){
        uri = uri + "?" + message;
    }
    if(method == "POST"){
        headers["Content-Type"] = "application/x-www-form-urlencoded";
        entity = message;
    }
    return { method: method, url: uri, headers: headers, entity:entity };
}

