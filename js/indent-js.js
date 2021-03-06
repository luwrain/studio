#!/usr/bin/jjs

var INDENT_STEP = 4;

var lines = [];
var f = new java.io.File("indent-js.js");
var r = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(f)));
var line = r.readLine();
while (line != null)
{
    lines.push(line);
    line = r.readLine();
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
    return 0;
}

function cutStringConstants(line)
{
    var res = "";
    for(var i = 0;i < line.length();i++)
    {
	if (line[i] == '\"')
	{
	    var j = i + 1;
	    while (j < line.length() && line[j] != '\"')
		if (line[j] == '\\')
		    j += 2; else
		j++;
	    if (j >= line.length())
		return res;
	    i = j;
	    continue;
	}
		if (line[i] == '\'')
	{
	    var j = i + 1;
	    while (j < line.length() && line[j] != '\'')
				if (line[j] == '\\')
		    j += 2; else
					j++;
	    if (j >= line.length())
		return res;
	    i = j;
	    continue;
	}
		res += line[i];
    }
    return res;
}

function getBracesBalance(line)
{
    var res = 0;
    for(var i = 0;i < line.length;i++)
    {
	if (line[i] == '{')
	    res++;
	if (line[i] == '}')
	    res--;
    }
    return res;
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

function getNewIndent(index)
{
    var prevLineIndex = findPrevLine(index);
    if (prevLineIndex < 0)
	return 0;
    var prevLine = lines[prevLineIndex];
    var prevLineIndex = getIndentOfLine(prevLine);
    var prevLineText = cutStringConstants(prevLine.trim());
    if (prevLineText.isEmpty())
	return 0;
    var bracesBalance = getBracesBalance(prevLineText);
    return getIndentOfLine(prevLine) + (bracesBalance * INDENT_STEP);
}

for(var i = 0;i < lines.length;i++)
    print(getNewIndent(i) + " " + lines[i]);
