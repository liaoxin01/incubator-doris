// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

suite("fold_constant_string_arithmatic") {
    sql "set enable_nereids_planner=true"
    sql "set enable_fallback_to_original_planner=false"
    sql "set enable_fold_constant_by_be=false"
    
    // append_trailing_char_if_absent
    testFoldConst("select append_trailing_char_if_absent('', '!')")
    testFoldConst("select append_trailing_char_if_absent(12345, '!')")
    testFoldConst("select append_trailing_char_if_absent('a','c')")
    testFoldConst("select append_trailing_char_if_absent('ac','c')")
    testFoldConst("select append_trailing_char_if_absent(cast('a' as string), cast('c' as string))")
    testFoldConst("select append_trailing_char_if_absent(cast('ac' as string), cast('c' as string))")
    testFoldConst("select append_trailing_char_if_absent('hello!', '!')")
    testFoldConst("select append_trailing_char_if_absent('hello', '😊')")
    testFoldConst("select append_trailing_char_if_absent('hello😊', '😊')")
    testFoldConst("select append_trailing_char_if_absent('hello😊', '(ಥ _ ಥ)')")
    testFoldConst("select append_trailing_char_if_absent('hello', ' ')")
    testFoldConst("select append_trailing_char_if_absent('hello', '')")
    testFoldConst("select append_trailing_char_if_absent('hello', '?')")
    testFoldConst("select append_trailing_char_if_absent('hello?', '!')")
    testFoldConst("select append_trailing_char_if_absent('hello', '1')")
    testFoldConst("select append_trailing_char_if_absent('hello', 1)")
    testFoldConst("select append_trailing_char_if_absent('hello', 'ab')")
    testFoldConst("select append_trailing_char_if_absent('hello', NULL)")
    testFoldConst("select append_trailing_char_if_absent('hello', 'ell')")
    testFoldConst("select append_trailing_char_if_absent('hello', 'ello')")
    testFoldConst("select append_trailing_char_if_absent('ello', 'hello')")
    testFoldConst("select append_trailing_char_if_absent('it','a')")
    testFoldConst("select append_trailing_char_if_absent(NULL, '!')")
    testFoldConst("select append_trailing_char_if_absent('This is a very long string', '.')")
    testFoldConst("select append_trailing_char_if_absent('Привет', '!')")
    testFoldConst("select append_trailing_char_if_absent('Привет', 'вет')")
    testFoldConst("select append_trailing_char_if_absent('こんにちは', '!')")
    testFoldConst("select append_trailing_char_if_absent('\n\t', '\n')")
    testFoldConst("select append_trailing_char_if_absent('こんにちは', 'ちは')")
    
    // ascii
    testFoldConst("select ascii('!')")
    testFoldConst("select ascii('1')")
    testFoldConst("select ascii('a')")
    testFoldConst("select ascii('A')")
    testFoldConst("select ascii('こ')")
    testFoldConst("select ascii('안')")
    testFoldConst("select ascii('안こ')")
    testFoldConst("select ascii('')")
    testFoldConst("select ascii('中')")
    
    // bin
    testFoldConst("select bin(5)")
    testFoldConst("select bin(-5)")
    testFoldConst("select bin(9223372036854775807)")
    testFoldConst("select bin(9223372036854775808)")
    testFoldConst("select bin(-9223372036854775809)")
    
    // bit_length
    testFoldConst("select bit_length('abc')")
    testFoldConst("select bit_length(cast('abc' as string))")
    testFoldConst("select bit_length('こんにちは世界')")
    testFoldConst("select bit_length('안녕하세요 세계!')")
    testFoldConst("select bit_length('')")
    
    // char
    testFoldConst("select char(65)")
    testFoldConst("select char(-1)")
    testFoldConst("select char(65535)")
    
    // character_length
    testFoldConst("select character_length(cast('Hello World' as string))")
    testFoldConst("select character_length('Hello World')")
    testFoldConst("select character_length('你好 世界')")
    testFoldConst("select character_length(' Hello World')")
    testFoldConst("select character_length('  你好 世界')")
    testFoldConst("select character_length('Hello World  ')")
    testFoldConst("select character_length('  你好 世界  ')")
    testFoldConst("select char_length('abc')")
    testFoldConst("select char_length(cast('abc' as string))")
    testFoldConst("select char_length('你好 世界')")
    testFoldConst("select char_length('  abc')")
    testFoldConst("select char_length('  你好 世界')")
    testFoldConst("select char_length('你好 世界  ')")

    // concat
    testFoldConst("select concat('a', 'b')")
    testFoldConst("select concat('a', 'b', 'c')")
    testFoldConst("select concat('a', null, 'c')")
    testFoldConst("select concat('Hello', NULL, 'World')")
    testFoldConst("select concat('Hello', ' ', 'World')")
    testFoldConst("select concat('你好', ' ', '世界')")
    testFoldConst("select concat('', '你好', ' ', '世界')")
    testFoldConst("select concat('你好', ' ', '世界', '')")
    
    // concat_ws
    testFoldConst("select concat_ws('-', '2024', '09', '02')")
    testFoldConst("select concat_ws('', '2024', '09', '02')")
    testFoldConst("select concat_ws('-', '', '2024', '09', '02')")
    testFoldConst("select concat_ws(NULL, ['d', 'is'])")
    testFoldConst("select concat_ws(NULL, 'd', 'is')")
    testFoldConst("select concat_ws('or', ['d', '','is'])")
    testFoldConst("select concat_ws('or', ['d', 'is'])")
    testFoldConst("select concat_ws('or', 'd', 'is')")
    testFoldConst("select concat_ws('or', ['d', NULL,'is'])")
    testFoldConst("select concat_ws('or', 'd', NULL,'is')")
    testFoldConst("select concat_ws(' ', '你好', '世界')")
    testFoldConst("select concat_ws(' ', [])")
    
    // elt
    testFoldConst("select elt(0, cast('hello' as string), cast('doris' as string))")
    testFoldConst("select elt(0, 'hello', 'doris')")
    testFoldConst("select elt(1, cast('hello' as string), cast('doris' as string))")
    testFoldConst("select elt(1, 'hello', 'doris')")
    testFoldConst("select elt(2, cast('hello' as string), cast('doris' as string))")
    testFoldConst("select elt(2, 'hello', 'doris')")
    testFoldConst("select elt(3, cast('hello' as string), cast('doris' as string))")
    testFoldConst("select elt(3, 'hello', 'doris')")
    testFoldConst("select c1, c2, elt(c1, c2) from (select number as c1, 'varchar' as c2 from numbers('number'='5') where number > 0) a")

    // ends_with
    testFoldConst("select ends_with(cast('Hello doris' as string), cast('doris' as string))")
    testFoldConst("select ends_with('Hello doris', 'doris')")
    testFoldConst("select ends_with('こんにちは世界！안녕하세요 세계', '안녕하세요 세계')")
    testFoldConst("select ends_with('안녕하세요 세계こんにちは世界！', 'こんにちは世界！')")
    testFoldConst("select ends_with('안녕하세요 세계こんにちは世界', 'こんにちは世界')")
    testFoldConst("select ends_with('안녕하세요 세계こんにちは世界', 'こんにちは')")
    testFoldConst("select ends_with('Hello doris', '')")
    testFoldConst("select ends_with('', 'Hello doris')")
    testFoldConst("select ends_with(null, 'Hello doris')")
    testFoldConst("select ends_with('Hello doris', null)")
    testFoldConst("select ends_with(' ', '')")
    testFoldConst("select ends_with(' ', ' ')")
    testFoldConst("select ends_with('', ' ')")
    testFoldConst("select ends_with('', '')")

    // field
    testFoldConst("select field('b', 'a', 'b', 'c')")
    testFoldConst("select field('d', 'a', 'b', 'c')")
    testFoldConst("select field('こ', 'ん', 'に', 'ち', 'こ')")
    testFoldConst("select field('=', '+', '=', '=', 'こ')")
    testFoldConst("select field('==', '+', '=', '==', 'こ')")
    testFoldConst("select field('=', '+', '==', '==', 'こ')")
    
    // find_in_set
    testFoldConst("select find_in_set('a', null)")
    testFoldConst("select find_in_set('b', 'a,b,c')")
    testFoldConst("select find_in_set('b', ' a,b,c')")
    testFoldConst("select find_in_set('b', 'a ,b,c')")
    testFoldConst("select find_in_set('b', 'a, b,c')")
    testFoldConst("select find_in_set('b', 'a,b,c ')")
    testFoldConst("select find_in_set('a,b,c ', 'a,b,c')")
    testFoldConst("select find_in_set('b', 'a,b,c')")
    testFoldConst("select find_in_set(cast('a' as string), NULL)")
    testFoldConst("select find_in_set(cast('b' as string), cast('a,b,c' as string))")
    testFoldConst("select find_in_set(cast('b' as string), cast('a,b,c' as string))")
    testFoldConst("select find_in_set(cast('d' as string), cast('a,b,c' as string))")
    testFoldConst("select find_in_set(cast('d' as string), cast('a,b,c' as string))")
    testFoldConst("select find_in_set('d', 'a,b,c')")
    testFoldConst("select find_in_set('d', 'a,b,c')")
    testFoldConst("select find_in_set(null, 'a,b,c')")
    testFoldConst("select find_in_set(NULL, cast('a,b,c' as string))")
    testFoldConst("SELECT find_in_set('A', '哈哈哈AAA')")
    testFoldConst("SELECT find_in_set('哈','哈哈哈AAA')")
    testFoldConst("SELECT find_in_set(' ','哈哈哈AAA')")
    testFoldConst("SELECT find_in_set('','哈哈哈AAA')")
    testFoldConst("SELECT find_in_set(',','a,')")
    testFoldConst("SELECT find_in_set(',','哈哈哈AAA')")
    
    // hex
    testFoldConst("select hex('@')")
    testFoldConst("select hex('1')")
    testFoldConst("select hex(-1)")
    testFoldConst("select hex(-1)")
    testFoldConst("select hex('12')")
    testFoldConst("select hex(12)")
    testFoldConst("select hex(12)")
    testFoldConst("select hex(-255)")
    testFoldConst("select hex(-255)")
    testFoldConst("select hex(255)")
    testFoldConst("select hex(255)")
    testFoldConst("select hex('A')")
    testFoldConst("select hex(cast('12' as string))")
    testFoldConst("select hex(cast('1' as string))")
    testFoldConst("select hex(cast('A' as string))")
    testFoldConst("select hex(cast('@' as string))")
    testFoldConst("select hex(cast('hello,doris' as string))")
    testFoldConst("select hex('hello,doris')")
    
    // ifnull
    testFoldConst("select ifnull(null,3)")
    testFoldConst("select ifnull(3,null)")
    testFoldConst("select ifnull(null,null)")
    
    // initcap
    testFoldConst("select initcap('AbC123abc abc.abc,?|abc')")
    testFoldConst("select initcap(cast('AbC123abc abc.abc,?|abc' as string))")
    testFoldConst("select initcap(cast('hello world' as string))")
    testFoldConst("select initcap('hello world')")
    testFoldConst("select initcap(' hello world')")
    testFoldConst("select initcap('こんにちは')")
    testFoldConst("select initcap('上海天津北京杭州')")
    
    // instr
    testFoldConst("select instr('上海天津北京杭州', '北京')")
    testFoldConst("select instr('abc', 'b')")
    testFoldConst("select instr('abc', 'd')")
    testFoldConst("select instr('abc', 'abcd')")
    testFoldConst("select instr('abc', null)")
    testFoldConst("select instr(cast('Hello' as string), cast('World' as string))")
    testFoldConst("select instr(cast('Hello World' as string), cast('World' as string))")
    testFoldConst("select instr('foobar', '')")
    testFoldConst("select instr('Hello', 'World')")
    testFoldConst("select instr('Hello World', 'World')")
    testFoldConst("select instr(null, 'a')")
    testFoldConst("select instr(NULL, cast('a' as string))")
    testFoldConst("select instr('', 'World')")
    
    // lcase
    testFoldConst("select lcase('AbC123')")
    testFoldConst("select lcase(cast('AbC123' as string))")
    testFoldConst("select lcase('上海天津北京杭州')")
    testFoldConst("select lcase('こんにちは')")
    
    // left
    testFoldConst("select left(CAST('good morning' AS STRING), 120)")
    testFoldConst("select left(CAST('good morning' AS STRING), -5)")
    testFoldConst("select left(CAST('good morning' AS STRING), NULL)")
    testFoldConst("select left(cast('Hello' as string), 10)")
    testFoldConst("select left(cast('Hello doris' as string), 5)")
    testFoldConst("select left(CAST('Hello doris' AS STRING), 5)")
    testFoldConst("select left(cast('Hello World' as string), 5)")
    testFoldConst("select left(CAST(NULL AS STRING), 1)")
    testFoldConst("select left('good morning', 120)")
    testFoldConst("select left('good morning', -5)")
    testFoldConst("select left('good morning', NULL)")
    testFoldConst("select left('Hello', 10)")
    testFoldConst("select left('', 10)")
    testFoldConst("select left(' Hello', 10)")
    testFoldConst("select left('Hello doris', 5)")
    testFoldConst("select left('Hello doris',5)")
    testFoldConst("select left('Hello World', 5)")
    testFoldConst("select left(NULL, 1)")
    testFoldConst("select left('上海天津北京杭州', 5)")
    testFoldConst("select left('上海天津北京杭州', -5)")
    testFoldConst("select left('上海天津北京杭州', 0)")
    
    // length
    testFoldConst("select length('你')")
    testFoldConst("select length('abc')")
    testFoldConst("select length(cast('abc' as string))")
    testFoldConst("select length(cast('Hello World' as string))")
    testFoldConst("select length('Hello World')")
    testFoldConst("select length('')")
    testFoldConst("select length(' Hello World')")
    testFoldConst("select length(space(10))")

    // locate
    testFoldConst("select locate('北京', '上海天津北京杭州')")
    testFoldConst("select locate('上海天津北京杭州', '北京')")
    testFoldConst("select locate('bar', 'foobarbar')")
    testFoldConst("select locate(cast('北京' as string), cast('上海天津北京杭州' as string))")
    testFoldConst("select locate(cast('' as string), cast('foobar' as string))")
    testFoldConst("select locate(cast('bar' as string), cast('foobarbar' as string))")
    testFoldConst("select locate(cast('World' as string), cast('Hello' as string))")
    testFoldConst("select locate(cast('World' as string), cast('Hello World' as string))")
    testFoldConst("select locate(cast('xbar' as string), cast('foobar' as string))")
    testFoldConst("select locate('', 'foobar')")
    testFoldConst("select locate('World', 'Hello')")
    testFoldConst("select locate('World', 'Hello World')")
    testFoldConst("select locate('xbar', 'foobar')")
    testFoldConst("select locate('北京', '上海天津北京杭州', 4)")
    testFoldConst("select locate('北京', '上海天津北京杭州', 5)")
    testFoldConst("select locate('北京', '上海天津北京杭州', -4)")
    testFoldConst("select locate('北京', '上海天津北京杭州', -5)")
    testFoldConst("select locate('2', '   123  ', 1)")
    
    // lower
    testFoldConst("select lower('AbC123')")
    testFoldConst("select lower(cast('AbC123' as string))")
    testFoldConst("select lower(cast('Hello World' as string))")
    testFoldConst("select lower('Hello World')")
    
    // lpad
    testFoldConst("select lpad(cast('hi' as string), 1, cast('xy' as string))")
    testFoldConst("select lpad(cast('hi' as string), 5, cast('xy' as string))")
    testFoldConst("select lpad('hi', 1, 'xy')")
    testFoldConst("select lpad('hi', 5, 'xy')")
    testFoldConst("select lpad('hi', 1, '')")
    testFoldConst("select lpad('', 1, 'xy')")
    testFoldConst("select lpad('hi', 1, ' ')")
    testFoldConst("select lpad(' ', 1, 'xy')")
    testFoldConst("select lpad(cast('北京' as string), 1, cast('杭州' as string))")
    testFoldConst("select lpad(cast('北京' as string), 5, cast('杭州' as string))")
    
    // ltrim
    testFoldConst("select ltrim(' 11111', 11)")
    testFoldConst("select ltrim('11111 ', 11)")
    testFoldConst("select ltrim('   ab d')")
    testFoldConst("select ltrim(cast(' 11111' as string), cast(11 as string))")
    testFoldConst("select ltrim(cast('11111 ' as string), cast(11 as string))")
    testFoldConst("select ltrim(cast('   ab d' as string))")
    testFoldConst("select ltrim(cast('Hello' as string))")
    testFoldConst("select ltrim(cast('  Hello World  ' as string))")
    testFoldConst("select ltrim('Hello')")
    testFoldConst("select ltrim('  Hello World  ')")
    testFoldConst("select ltrim('  上海天津北京杭州  ')")
    
    // md5
    testFoldConst("select md5(cast('Hello World' as string))")
    testFoldConst("select md5('Hello World')")
    testFoldConst("select md5(' Hello World')")
    testFoldConst("select md5('Hello World ')")
    testFoldConst("select md5('')")
    testFoldConst("select md5('こんにちは')")
    testFoldConst("select md5sum('Hello World')")
    testFoldConst("select md5sum('こんにちは')")
    testFoldConst("select md5sum('===*+-')")

    // money_format
    testFoldConst("select money_format(1123.4)")
    testFoldConst("select money_format(1123.456)")
    testFoldConst("select money_format(17014116)")
    testFoldConst("select money_format(truncate(1000,10))")
    testFoldConst("select money_format(-1123.4)")
    testFoldConst("select money_format(-1123.456)")
    testFoldConst("select money_format(-17014116)")
    testFoldConst("select money_format(-truncate(1000,10))")
    
    // not_null_or_empty
    testFoldConst("select not_null_or_empty('')")
    testFoldConst("select not_null_or_empty('a')")
    testFoldConst("select not_null_or_empty(cast('a' as string))")
    testFoldConst("select not_null_or_empty(cast('' as string))")
    testFoldConst("select not_null_or_empty(cast('       ' as string))")
    testFoldConst("select not_null_or_empty(null)")
    testFoldConst("select not_null_or_empty(NULL)")
    testFoldConst("select not_null_or_empty('\b')")
    testFoldConst("select not_null_or_empty(' \b')")
    
    // null_or_empty
    testFoldConst("select null_or_empty('')")
    testFoldConst("select null_or_empty('a')")
    testFoldConst("select null_or_empty(cast('a' as string))")
    testFoldConst("select null_or_empty(cast('' as string))")
    testFoldConst("select null_or_empty(null)")
    testFoldConst("select null_or_empty(NULL)")
    testFoldConst("select null_or_empty('\b')")
    testFoldConst("select null_or_empty(' \b')")
    
    // parse_url
    testFoldConst("select parse_url(cast('http://www.example.com/path?query=abc' as string), cast('HOST' as string))")
    testFoldConst("select parse_url('http://www.example.com/path?query=abc', 'HOST')")
    testFoldConst("select parse_url('http://www.example.com/path?query=abc', 'QUERY')")
    testFoldConst("select parse_url('http://www.example.com/path?query=こんにちは', 'QUERY')")
    testFoldConst("select parse_url(\"http://www.example.com/path?query=a\b\'\", 'QUERY')")
    testFoldConst("select parse_url(\"http://www.example.com/path.query=a\b\'\", 'QUERY')")

    // repeat
    testFoldConst("select repeat('a', 0)")
    testFoldConst("select repeat('a', -1)")
    testFoldConst("select repeat('a', 3)")
    testFoldConst("select repeat('a',null)")
    testFoldConst("select repeat(cast('a' as string), 0)")
    testFoldConst("select repeat(cast('a' as string), -1)")
    testFoldConst("select repeat(cast('a' as string), 3)")
    testFoldConst("select repeat(cast('Hello' as string), -3)")
    testFoldConst("select repeat(cast('Hello' as string), 3)")
    testFoldConst("select repeat('Hello', -3)")
    testFoldConst("select repeat('Hello', 3)")
    testFoldConst("select repeat(NULL, 1)")
    testFoldConst("select repeat('', 3)")
    testFoldConst("select repeat(' ', 3)")
    testFoldConst("select repeat('前进',4)")
    
    // replace
    testFoldConst("select replace(cast('Hello World' as string), '', cast('Everyone' as string))")
    testFoldConst("select replace(cast('Hello World' as string), cast('World' as string), '')")
    testFoldConst("select replace(cast('Hello World' as string), cast('World' as string), cast('Everyone' as string))")
    testFoldConst("select replace(cast('https://doris.apache.org:9090' as string), cast(':9090' as string), cast('' as string))")
    testFoldConst("select replace(cast('https://doris.apache.org:9090' as string), cast('' as string), cast('new_str' as string))")
    testFoldConst("select replace('Hello World', '', 'Everyone')")
    testFoldConst("select replace('Hello World', 'World', '')")
    testFoldConst("select replace('Hello World', 'World', 'Everyone')")
    testFoldConst("select replace('https://doris.apache.org:9090', ':9090', '')")
    testFoldConst("select replace('https://doris.apache.org:9090', '', 'new_str')")
    testFoldConst("select replace('https://doris.apache.org:9090', './*', 'new_str')")
    
    // reverse
    testFoldConst("select reverse('Hello')")
    testFoldConst("select reverse('')")
    testFoldConst("select reverse('こんにちは')")
    
    // right
    testFoldConst("select right(CAST('good morning' AS STRING), NULL)")
    testFoldConst("select right(cast('Hello' as string), 10)")
    testFoldConst("select right(CAST('Hello doris' AS STRING), 120)")
    testFoldConst("select right(cast('Hello doris' as string), 5)")
    testFoldConst("select right(CAST('Hello doris' AS STRING), 5)")
    testFoldConst("select right(CAST('Hello doris' AS STRING), -6)")
    testFoldConst("select right(cast('Hello World' as string), 5)")
    testFoldConst("select right(CAST(NULL AS STRING), 1)")
    testFoldConst("select right('good morning', NULL)")
    testFoldConst("select right('Hello', 10)")
    testFoldConst("select right('Hello doris', 120)")
    testFoldConst("select right('Hello doris', 5)")
    testFoldConst("select right('Hello doris',5)")
    testFoldConst("select right('Hello doris', -6)")
    testFoldConst("select right('Hello World', 5)")
    testFoldConst("select right('Hello World', 0)")
    testFoldConst("select right(NULL, 1)")
    
    // rpad
    testFoldConst("select rpad(cast('hi' as string), 1, cast('xy' as string))")
    testFoldConst("select rpad(cast('hi' as string), 5, cast('xy' as string))")
    testFoldConst("select rpad('hi', 1, 'xy')")
    testFoldConst("select rpad('hi', 5, 'xy')")
    
    // rtrim
    testFoldConst("select rtrim(' 11111', 11)")
    testFoldConst("select rtrim('11111 ', 11)")
    testFoldConst("select rtrim(cast(' 11111' as string), cast(11 as string))")
    testFoldConst("select rtrim(cast('11111 ' as string), cast(11 as string))")
    testFoldConst("select rtrim(cast('Hello' as string))")
    testFoldConst("select rtrim(cast('  Hello World  ' as string))")
    testFoldConst("select rtrim('Hello')")
    testFoldConst("select rtrim('  Hello World  ')")
    
    // space
    testFoldConst("select space(-5)")
    testFoldConst("select space(5)")
    testFoldConst("select space(0)")

    // split_by_string
    testFoldConst("select split_by_string('::', 'abc')")
    testFoldConst("select split_by_string('a::b::c', '::')")
    testFoldConst("select split_by_string(cast('a::b::c' as string), cast('::' as string))")
    testFoldConst("select split_by_string(cast('abc' as string), cast('::' as string))")
    testFoldConst("select split_by_string('上海天津北京杭州', '北')")
    testFoldConst("select split_by_string('abccccc', 'c')")
    
    // split_part
    testFoldConst("select split_part('a,b,c', ',', -1)")
    testFoldConst("select split_part('abc##123###xyz', '##', 0)")
    testFoldConst("select split_part('abc##123###xyz', '##', -1)")
    testFoldConst("select split_part('abc##123###xyz', '##', 1)")
    testFoldConst("select split_part('abc##123###xyz', '##', -2)")
    testFoldConst("select split_part('abc##123###xyz', '##', 3)")
    testFoldConst("select split_part('abc##123###xyz', '##', -4)")
    testFoldConst("select split_part('abc##123###xyz', '##', 5)")
    testFoldConst("select split_part('a,b,c', ',', 2)")
    testFoldConst("select split_part('a,b,c', ',', 5)")
    testFoldConst("select split_part(cast('a,b,c' as string), cast(',' as string), -1)")
    testFoldConst("select split_part(cast('a,b,c' as string), cast(',' as string), 2)")
    testFoldConst("select split_part(cast('a,b,c' as string), cast(',' as string), 5)")
    testFoldConst("select split_part(cast('hello world' as string), cast(' ' as string), 1)")
    testFoldConst("select split_part(cast('hello world' as string), cast(' ' as string), 2)")
    testFoldConst("select split_part(cast('hello world' as string), cast(' ' as string), 3)")
    testFoldConst("select split_part('hello world', ' ', 0)")
    testFoldConst("select split_part('hello world', ' ', -1)")
    testFoldConst("select split_part('hello world', ' ', 1)")
    testFoldConst("select split_part('hello world', ' ', -2)")
    testFoldConst("select split_part('hello world', ' ', 2)")
    testFoldConst("select split_part('hello world', ' ', -3)")
    testFoldConst("SELECT split_part('哈哈哈AAA','A', -5)")
    testFoldConst("SELECT split_part('哈哈哈AAA','A', -4)")
    testFoldConst("SELECT split_part('哈哈哈AAA','A', -3)")
    testFoldConst("SELECT split_part('哈哈哈AAA','A', -2)")
    testFoldConst("SELECT split_part('哈哈哈AAA','A', -1)")
    testFoldConst("SELECT split_part('哈哈哈AAA','A', 0)")
    testFoldConst("SELECT split_part('哈哈哈AAA','A', 1)")
    testFoldConst("SELECT split_part('哈哈哈AAA','A', 2)")
    testFoldConst("SELECT split_part('哈哈哈AAA','A', 3)")
    testFoldConst("SELECT split_part('哈哈哈AAA','A', 4)")
    testFoldConst("SELECT split_part('哈哈哈AAA','A', 5)")
    testFoldConst("SELECT split_part('哈哈哈AA+','A', -4)")
    testFoldConst("SELECT split_part('哈哈哈AA+','A', -3)")
    testFoldConst("SELECT split_part('哈哈哈AA+','A', -2)")
    testFoldConst("SELECT split_part('哈哈哈AA+','A', -1)")
    testFoldConst("SELECT split_part('哈哈哈AA+','A', 0)")
    testFoldConst("SELECT split_part('哈哈哈AA+','A', 1)")
    testFoldConst("SELECT split_part('哈哈哈AA+','A', 2)")
    testFoldConst("SELECT split_part('哈哈哈AA+','A', 3)")
    testFoldConst("SELECT split_part('哈哈哈AA+','A', 4)")
    
    // starts_with
    testFoldConst("select starts_with('hello world','hello')")
    testFoldConst("select starts_with('hello world',null)")
    testFoldConst("select starts_with('hello world','world')")
    testFoldConst("select starts_with(' hello world','world')")
    testFoldConst("select starts_with('上海天津北京杭州','上海')")
    testFoldConst("select starts_with('上海天津北京杭州','北京')")
    
    // strcmp
    testFoldConst("select strcmp('a', 'abc')")
    testFoldConst("select strcmp('abc', 'abc')")
    testFoldConst("select strcmp('abc', 'abc')")
    testFoldConst("select strcmp('abc', 'abd')")
    testFoldConst("select strcmp('abcd', 'abc')")
    testFoldConst("select strcmp('abc', NULL)")
    testFoldConst("select strcmp(CAST('a' AS STRING), CAST('abc' AS STRING))")
    testFoldConst("select strcmp(cast('abc' as string), cast('abc' as string))")
    testFoldConst("select strcmp(CAST('abc' AS STRING), CAST('abc' AS STRING))")
    testFoldConst("select strcmp(cast('abc' as string), cast('abd' as string))")
    testFoldConst("select strcmp(cast('abc' as string), NULL)")
    testFoldConst("select strcmp(CAST('abcd' AS STRING), CAST('abc' AS STRING))")
    
    // strleft
    testFoldConst("select strleft('good morning', 120)")
    testFoldConst("select strleft('good morning', -5)")
    testFoldConst("select strleft('good morning', NULL)")
    testFoldConst("select strleft('Hello doris', 5)")
    testFoldConst("select strleft('Hello World', 5)")
    testFoldConst("select strleft(' Hello World', 5)")
    testFoldConst("select strleft('Hello World ', 50)")
    testFoldConst("select strleft(NULL, 1)")
    
    // strright
    testFoldConst("select strright('good morning', NULL)")
    testFoldConst("select strright('Hello doris', 120)")
    testFoldConst("select strright('Hello doris', -5)")
    testFoldConst("select strright('Hello doris', 5)")
    testFoldConst("select strright('Hello World', 5)")
    testFoldConst("select strright(' Hello World', 5)")
    testFoldConst("select strright('Hello World  ', 5)")
    testFoldConst("select strright(NULL, 1)")
    
    // sub_replace
    testFoldConst("select sub_replace(CAST('doris' AS STRING), CAST('***' AS STRING), 1, 2)")
    testFoldConst("select sub_replace(CAST('doris' AS STRING), CAST('***' AS STRING), 1, 2)")
    testFoldConst("select sub_replace(CAST('this is origin str' AS STRING), CAST('NEW-STR' AS STRING), 1)")
    testFoldConst("select sub_replace(CAST('this is origin str' AS STRING), CAST('NEW-STR' AS STRING), 1)")
    testFoldConst("select sub_replace('doris','***',1,2)")
    testFoldConst("select sub_replace('doris','***',1,2)")
    testFoldConst("select sub_replace('this is origin str','NEW-STR',1)")
    testFoldConst("select sub_replace('this is origin str','NEW-STR',1)")
    testFoldConst("select sub_replace(CAST('doris' AS STRING), CAST('***' AS STRING), -1, 2)")
    testFoldConst("select sub_replace('上海天津北京杭州', '天津', 3, 4)")
    testFoldConst("select sub_replace('上海天津北京杭州', '天津', 30, 4)")
    
    // substr
    testFoldConst("select substr('a',0,1)")
    testFoldConst("select substr('a',-1,1)")
    testFoldConst("select substr('a',1,1)")
    testFoldConst("select substr('a',-2,1)")
    testFoldConst("select substr('a',2,1)")
    testFoldConst("select substr('a',-3,1)")
    testFoldConst("select substr('a',3,1)")
    testFoldConst("select substr('abcdef',-3,-1)")
    testFoldConst("select substr('abcdef',3,-1)")
    testFoldConst("select substr('',3,-1)")
    testFoldConst("select substr('abcdef',3,10)")

    // substring
    testFoldConst("select substring('1', 1, 1)")
    testFoldConst("select substring('abc1', -2)")
    testFoldConst("select substring('abc1', 2)")
    testFoldConst("select substring('abc1', 5)")
    testFoldConst("select substring('abc1def', 2, 2)")
    testFoldConst("select substring('abcdef',10,1)")
    testFoldConst("select substring('abcdef',-3,-1)")
    testFoldConst("select substring('abcdef',3,-1)")
    testFoldConst("select substring(cast('1' as string), 1, 1)")
    testFoldConst("select substring(CAST('abc1' AS STRING), -2)")
    testFoldConst("select substring(CAST('abc1' AS STRING), 2)")
    testFoldConst("select substring(CAST('abc1' AS STRING), 5)")
    testFoldConst("select substring(CAST('abc1def' AS STRING), 2, 2)")
    testFoldConst("select substring(CAST('abcdef' AS STRING), 10, 1)")
    testFoldConst("select substring(CAST('abcdef' AS STRING), -3, -1)")
    testFoldConst("select substring(CAST('abcdef' AS STRING), 3, -1)")
    testFoldConst("select substring(cast('Hello' as string), 1, 10)")
    testFoldConst("select substring(cast('Hello World' as string), -1, 5)")
    testFoldConst("select substring(cast('Hello World' as string), 1, 5)")
    testFoldConst("select substring('Hello', 1, 10)")
    testFoldConst("select substring('Hello World', -1, 5)")
    testFoldConst("select substring('Hello World', 1, 5)")
    testFoldConst("select substring('', 1, 5)")
    testFoldConst("select substring('Hello World', 1, 50)")

    // substring_index
    testFoldConst("select substring_index('a,b,c', ',', 2)")
    testFoldConst("select substring_index('a,b,c', '', 2)")
    testFoldConst("select substring_index(cast('a,b,c' as string), cast(',' as string), 2)")
    testFoldConst("select substring_index(CAST('hello world' AS STRING), CAST(' ' AS STRING), -1)")
    testFoldConst("select substring_index(CAST('hello world' AS STRING), CAST(' ' AS STRING), 1)")
    testFoldConst("select substring_index(CAST('hello world' AS STRING), CAST(' ' AS STRING), -2)")
    testFoldConst("select substring_index(CAST('hello world' AS STRING), CAST(' ' AS STRING), 2)")
    testFoldConst("select substring_index(CAST('hello world' AS STRING), CAST(' ' AS STRING), -3)")
    testFoldConst("select substring_index(CAST('hello world' AS STRING), CAST(' ' AS STRING), 3)")
    testFoldConst("select substring_index(CAST(NULL AS STRING), CAST('__' AS STRING), 1)")
    testFoldConst("select substring_index(CAST('prefix_string2' AS STRING), CAST('__' AS STRING), 1)")
    testFoldConst("select substring_index(CAST('prefix__string2' AS STRING), CAST('_' AS STRING), 2)")
    testFoldConst("select substring_index(CAST('prefix__string2' AS STRING), CAST('__' AS STRING), 2)")
    testFoldConst("select substring_index(CAST('prefix_string' AS STRING), CAST('__' AS STRING), -1)")
    testFoldConst("select substring_index(CAST('prefix_string' AS STRING), CAST('_' AS STRING), NULL)")
    testFoldConst("select substring_index(CAST('prefix_string' AS STRING), CAST(NULL AS STRING), 1)")
    testFoldConst("select substring_index('hello world', ' ', -1)")
    testFoldConst("select substring_index('hello world', ' ', 1)")
    testFoldConst("select substring_index('hello world', ' ', -2)")
    testFoldConst("select substring_index('hello world', ' ', 2)")
    testFoldConst("select substring_index('hello world', ' ', -3)")
    testFoldConst("select substring_index('hello world', ' ', 3)")
    testFoldConst("select substring_index(null, '__', 1)")
    testFoldConst("select substring_index('prefix_string', '__', -1)")
    testFoldConst("select substring_index('prefix_string2', '__', 1)")
    testFoldConst("select substring_index('prefix__string2', '_', 2)")
    testFoldConst("select substring_index('prefix__string2', '__', 2)")
    testFoldConst("select substring_index('prefix_string', '_', null)")
    testFoldConst("select substring_index('prefix_string', null, 1)")
    testFoldConst("SELECT substring_index('哈哈哈AAA','A', -5)")
    testFoldConst("SELECT substring_index('哈哈哈AAA','A', -4)")
    testFoldConst("SELECT substring_index('哈哈哈AAA','A', -3)")
    testFoldConst("SELECT substring_index('哈哈哈AAA','A', -2)")
    testFoldConst("SELECT substring_index('哈哈哈AAA','A', -1)")
    testFoldConst("SELECT substring_index('哈哈哈AAA','A', 0)")
    testFoldConst("SELECT substring_index('哈哈哈AAA','A', 1)")
    testFoldConst("SELECT substring_index('哈哈哈AAA','A', 2)")
    testFoldConst("SELECT substring_index('哈哈哈AAA','A', 3)")
    testFoldConst("SELECT substring_index('哈哈哈AAA','A', 4)")
    testFoldConst("SELECT substring_index('哈哈哈AAA','A', 5)")
    testFoldConst("SELECT substring_index('哈哈哈AA+','A', -4)")
    testFoldConst("SELECT substring_index('哈哈哈AA+','A', -3)")
    testFoldConst("SELECT substring_index('哈哈哈AA+','A', -2)")
    testFoldConst("SELECT substring_index('哈哈哈AA+','A', -1)")
    testFoldConst("SELECT substring_index('哈哈哈AA+','A', 0)")
    testFoldConst("SELECT substring_index('哈哈哈AA+','A', 1)")
    testFoldConst("SELECT substring_index('哈哈哈AA+','A', 2)")
    testFoldConst("SELECT substring_index('哈哈哈AA+','A', 3)")
    testFoldConst("SELECT substring_index('哈哈哈AA+','A', 4)")

    // trim
    testFoldConst("select trim('11111', 11)")
    testFoldConst("select trim(cast('11111' as string), cast(11 as string))")
    testFoldConst("select trim(cast('  Hello World  ' as string))")
    testFoldConst("select trim('  Hello World  ')")

    // unhex
    testFoldConst("select unhex('')")
    testFoldConst("select unhex('@')")
    testFoldConst("select unhex('41')")
    testFoldConst("select unhex('4142')")
    testFoldConst("select unhex('68656C6C6F2C646F726973')")
    testFoldConst("select unhex(cast('4142' as string))")
    testFoldConst("select unhex(cast('41' as string))")
    testFoldConst("select unhex(cast('68656C6C6F2C646F726973' as string))")
    testFoldConst("select unhex(cast('' as string))")
    testFoldConst("select unhex(cast('@' as string))")
    testFoldConst("select unhex(cast('FF' as string))")
    testFoldConst("select unhex(cast('GHIJ' as string))")
    testFoldConst("select unhex('FF')")
    testFoldConst("select unhex('GHIJ')")
    testFoldConst("select unhex(NULL)")
    testFoldConst("select unhex(NULL)")
    testFoldConst("select upper(cast('Hello World' as string))")
    testFoldConst("select upper('Hello World')")

    // url_decode url_encode
    testFoldConst("select url_decode(cast('http%3A%2F%2Fwww.apache.org%2Flicenses%2FLICENSE-2.0' as string))")
    testFoldConst("select url_decode('http%3A%2F%2Fwww.apache.org%2Flicenses%2FLICENSE-2.0')")
    testFoldConst("select url_decode('http%3A%2F%2Fwww.apache.org%2Flicenses%2FLICENSE-22.0')")
    testFoldConst("select url_encode('http://www.apache.org/licenses/LICENSE-2.0')")
    testFoldConst("select url_encode(' http://www.apache.org/licenses/LICENSE-2.0 ')")

    // Normal Usage Test Cases

    // Test Case 1: Append missing trailing character
    testFoldConst("select append_trailing_char_if_absent('hello', '!')")
    // Expected Output: 'hello!'

    // Test Case 2: Trailing character already present
    testFoldConst("select append_trailing_char_if_absent('hello!', '!')")
    // Expected Output: 'hello!'

    // Test Case 3: Append trailing space
    testFoldConst("select append_trailing_char_if_absent('hello', ' ')")
    // Expected Output: 'hello '

    // Test Case 4: Empty string input
    testFoldConst("select append_trailing_char_if_absent('', '!')")
    // Expected Output: '!'

    // Test Case 5: Append different character
    testFoldConst("select append_trailing_char_if_absent('hello', '?')")
    // Expected Output: 'hello?'

    // Test Case 6: String ends with a different character
    testFoldConst("select append_trailing_char_if_absent('hello?', '!')")
    // Expected Output: 'hello?!'

    // Edge and Unusual Usage Test Cases

    // Test Case 7: Input is NULL
    testFoldConst("select append_trailing_char_if_absent(NULL, '!')")
    // Expected Output: NULL

    // Test Case 8: Trailing character is NULL
    testFoldConst("select append_trailing_char_if_absent('hello', NULL)")
    // Expected Output: NULL

    // Test Case 9: Empty trailing character
    testFoldConst("select append_trailing_char_if_absent('hello', '')")
    // Expected Output: Error or no change depending on implementation

    // Test Case 10: Trailing character is more than 1 character long
    testFoldConst("select append_trailing_char_if_absent('hello', 'ab')")
    // Expected Output: Error

    // Test Case 11: Input string is a number
    testFoldConst("select append_trailing_char_if_absent(12345, '!')")
    // Expected Output: Error or '12345!'

    // Test Case 12: Trailing character is a number
    testFoldConst("select append_trailing_char_if_absent('hello', '1')")
    // Expected Output: 'hello1'

    // Test Case 13: Input is a single character
    testFoldConst("select append_trailing_char_if_absent('h', '!')")
    // Expected Output: 'h!'

    // Test Case 14: Unicode character as input and trailing character
    testFoldConst("select append_trailing_char_if_absent('こんにちは', '!')")
    // Expected Output: 'こんにちは!'

    // Test Case 15: Multibyte character as trailing character
    testFoldConst("select append_trailing_char_if_absent('hello', '😊')")
    // Expected Output: 'hello😊'

    // Test Case 16: Long string input
    testFoldConst("select append_trailing_char_if_absent('This is a very long string', '.')")
    // Expected Output: 'This is a very long string.'

    // Error Handling Test Cases

    // Test Case 17: Invalid trailing character data type (numeric)
    testFoldConst("select append_trailing_char_if_absent('hello', 1)")
    // Expected Output: Error

    // Test Case 18: Invalid input data type (integer)
    testFoldConst("select append_trailing_char_if_absent(12345, '!')")
    // Expected Output: Error or '12345!'

    // Test Case 19: Non-ASCII characters
    testFoldConst("select append_trailing_char_if_absent('Привет', '!')")
    // Expected Output: 'Привет!'

    // Test Case 20: Trailing character with whitespace
    testFoldConst("select append_trailing_char_if_absent('hello', ' ')")
    // Expected Output: 'hello '


}
