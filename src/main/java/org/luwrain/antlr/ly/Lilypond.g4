
grammar Lilypond;

@header{
package org.luwrain.antlr.ly;
}

score
    : command* EOF
    ;

command
    : '\\' IDENT value*
    ;

value
    : '{' IDENT '}'
    ;

IDENT
    :   [A-Za-z0-9]+
    ;

WS
    :   [ \t\r\n]+ -> skip
    ;
