/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

grammar Directives;

@header {
}

@members {
}

// Root rule for a directive
parse
  : directive EOF
  ;

// A directive is a name followed by zero or more arguments
directive
  : IDENTIFIER arguments?
  ;

// Arguments for a directive
arguments
  : argument+
  ;

// An argument can be a column reference, a string, a number, a property map, a byte size, or a time duration
argument
  : column_name       #ColumnNameArg
  | string            #StringArg
  | number            #NumberArg
  | boolean           #BooleanArg
  | property          #PropertyArg
  | byte_size         #ByteSizeArg
  | time_duration     #TimeDurationArg
  ;

// Column name with leading colon
column_name
  : COLON IDENTIFIER
  ;

// String literal with quotes
string
  : STRING
  ;

// Numeric literal
number
  : DECIMAL #IntegerLiteral
  | FLOAT   #FloatingPointLiteral
  ;

// Boolean literal
boolean
  : BOOLEAN
  ;

// Property map enclosed in {}
property
  : PROPERTY
  ;

// Byte size value with unit
byte_size
  : BYTE_SIZE
  ;

// Time duration value with unit
time_duration
  : TIME_DURATION
  ;

// Lexer rules

// Identifiers for directives and column names
IDENTIFIER
  : [a-zA-Z_][a-zA-Z0-9_-]*
  ;

// String literal in single or double quotes
STRING
  : '"' (~["\\\r\n] | '\\' [\\"])* '"'
  | '\'' (~['\\\r\n] | '\\' [\\'])* '\''
  ;

// Decimal integer
DECIMAL
  : '-'? [0-9]+
  ;

// Floating point number
FLOAT
  : '-'? [0-9]+ '.' [0-9]* EXPONENT?
  | '-'? '.' [0-9]+ EXPONENT?
  | '-'? [0-9]+ EXPONENT
  ;

// Exponent notation
fragment EXPONENT
  : [eE] [+-]? [0-9]+
  ;

// Boolean literals
BOOLEAN
  : 'true'
  | 'false'
  ;

// Property map with key-value pairs
PROPERTY
  : '{' (~[{}] | PROPERTY)* '}'
  ;

// Colon for column references
COLON
  : ':'
  ;

// Byte size pattern matching (e.g., 5KB, 2.5MB, 1GB)
BYTE_SIZE
  : [0-9]+ ('.' [0-9]+)? WS? SIZE_UNIT
  ;

// Size units
fragment SIZE_UNIT
  : [KkMmGgTtPp]? [Bb]
  ;

// Time duration pattern matching (e.g., 500ms, 1.5s, 2h)
TIME_DURATION
  : [0-9]+ ('.' [0-9]+)? WS? TIME_UNIT
  ;

// Time units
fragment TIME_UNIT
  : 'ns'  // nanoseconds
  | 'μs'  // microseconds
  | 'ms'  // milliseconds
  | 's'   // seconds
  | 'm'   // minutes
  | 'h'   // hours
  | 'd'   // days
  ;

// Whitespace
WS
  : [ \t\r\n]+ -> skip
  ;

// Line comment
COMMENT
  : '#' ~[\r\n]* -> skip
  ;