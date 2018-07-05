#!/usr/bin/jjs

var lines = [];
var f = new java.io.File("indent-js.js");
var r = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(f)));
var line = r.readLine();
while (line != null)
{
    lines.push(line);
    line = r.readLine();
}

function findPrevLine(index)
{
    var prevLine = index - 1;
    while (prevLine >= 0 && lines[prevLine].trim().isEmpty())
	prevLine--;
    if (prevLine < 0)
	return -1;
    return prevLine;
}

function getIndentOfLine(line)
{
    var res = 0;
    for(var i = 0;i < line.length();i++)
    {
	if (line[i] == '\t')
	    res += 8; else
		if (line[i] == ' ')
		    res += 1; else
			return res;
    }
    return -2;
}

function getNewIndent(index)
{
    var prevLineIndex = findPrevLine(index);
    if (prevLineIndex < 0)
	return 0;
    var prevLine = lines[prevLineIndex];
    var prevLineIndex = getIndentOfLine(prevLine);
    var prevLineText = prevLine.trim();
    if (prevLineText.isEmpty())//must never happen
	return 0;
    
    return getIndentOfLine(index);
}

for(var i = 0;i < lines.length;i++)
    print(getNewIndent(i) + ":" + lines[i].trim());
