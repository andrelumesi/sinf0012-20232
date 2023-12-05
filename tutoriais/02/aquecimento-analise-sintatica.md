# Introdução a Análise Sintática no PLY - Primeiros Passos


## 1. Objetivos da Atividade:
1. Apresentar como utilizar a ferramenta PLY para realização da etapa de análise sintática.
2. Como mapear uma gramática livre de contexto para PLY.

### Etapa 1 -  Análise Sintática no PLY.

O PLY pode gerar analisadores sintáticos a partir de uma gramática livre de contexto. 
Essa etapa pode demandar retrabalho, pois a gramática livre de contexto deve ser livre de conflitos shift/reduce ou reduce/reduce.

Para compreensão de como utilizar o PLY, considere a linguagem expressionLanguage, uma linguagem restrita a reconhecer expressões. Em expressionLanguage, uma expressão é definida pelas seguintes regras:```

```
exp → exp + exp 
            | exp * exp 
            | exp / exp 
            | exp - exp 
            | num 
```

Antes de prosseguirmos, são necessários dois passos. O primeiro, definir o léxico dessa linguagem. Já o segundo consiste em eliminar a ambiguidade dessa gramática. A solução para o primeiro passo é dada na página a seguir.

```
# -------------------------
# lingexpslex.py
#----------------------
import ply.lex as lex

tokens = ('SOMA', 'NUMERO', 'SUBTRACAO', 'DIVISAO', 'MULTIPLICACAO')
t_SOMA = r'\+'
t_SUBTRACAO = r'-'
t_DIVISAO = r'/'
t_MULTIPLICACAO = r'\*'


def t_NUMERO(t):
  r'\d+'
  t.value = int(t.value)
  return t


def t_newline(t):
  r'\n+'
  t.lexer.lineno += len(t.value)


t_ignore = ' \t\n'


def t_error(t):
  print("Illegal character '%s'" % t.value[0])
  t.lexer.skip(1)


lexer = lex.lex()
#
# # Test it out
data = '''
 3 + 4 - 10 + 20 - 2
 '''
lexer.input(data)

```


Para o segundo passo, temos de retirar a ambiguidade. A seguir, a solução para esse problema:

```
exp  → exp + exp1 | exp - exp1 | exp1 
exp1 → exp1 * exp2 | exp1 / exp2 | exp2
exp2 → num
```



TRADUÇÃO PARA PLY

Tomemos como exemplo as regras: 

```
exp →   exp + exp1 
      | exp - exp1
      | exp1 

```

A tradução para PLY é feita da seguinte forma:

```
def p_exp_soma(p):
    '''exp : exp SOMA exp1
           | exp SUBTRACAO exp1
           | exp1'''
```

Também é possível fazer cada regra separada:
 
```
def p_exp_soma(p):
    '''exp : exp SOMA exp1'''

def p_exp_subtracao(p):
    '''exp : exp SUBTRACAO exp1'''

def p_exp_exp1(p):
   '''exp : exp1'''

```

A seguir, apresentamos o código inicial do parser, que já apresenta os casos citados. Ao final do código, é mostrado  como construir o parser e, adicionalmente, como inicializar a análise sintaxe.


```
# -------------------------
# calcparser.py
#----------------------

import ply.yacc as yacc
import ply.lex as lex
from ExpressionLanguageLex import tokens

def p_exp_soma(p):
    '''exp : exp SOMA exp1'''

def p_exp_subtracao(p):
    '''exp : exp SUBTRACAO exp1'''

def p_exp_exp1(p):
   '''exp : exp1'''

parser = yacc.yacc()              #Criação do analisador sintático
 
result = parser.parse(debug=True) #Execução da análise sintática.

```


**Exercício 1 [calcparser.py]**: Execute o código anterior. A execução foi bem sucedida? 

**Exercício 2 [calcparser.py]**: Mapeie as demais regras da gramática citada. As regras que devem ser mapeadas são a de multiplicação, divisão e a relativa a num 

```
exp → exp + exp1 | exp - exp1 | exp1 
exp1 → exp1 * exp2 | exp1 / exp2 | exp2
exp2 → num 
```


**Exercício 3 [calcparser.py]**: Execute novamente o código com as mudanças. A execução foi bem sucedida?



### Etapa 2: Embutindo comportamento durante a execução da análise.


Uma característica interessante do analisador sintático gerado por PLY é que podemos embutir comportamentos durante a execução da análise. No exemplo anterior, dado que criamos um analisador para reconhecer expressões de soma e subtração, poderíamos criar uma calculadora.

Para isso, basta adicionarmos código aos métodos que representam as regras da gramática mapeada. Por exemplo, para darmos suporte a soma, basta fazermos a seguinte mudança:


```
def p_exp_soma(p):
    '''exp : exp SOMA exp1'''
    p[0] = p[1] + p[3]
```

Para entender o que foi feito, é necessário explicar o que é a variável p. No PLY, a variável p é uma lista que contém todos os elementos que compõem a regra. Assim, para a regra exp :  exp SOMA exp1, temos:
p[0], representa o elemento exp, que aparece antes dos dois pontos. 
p[1], representa o elemento exp, que aparece após os dois pontos.
p[2], representa o token SOMA
p[3], representa exp1.
Assim, após receber uma expressão de soma, PLY somará exp e exp1. Em seguida, devolverá esse resultado, através de p[0].   




**Exercício 4 [calcparser.py]**: Introduza a mudança de p_exp_soma(p) ao código. Execute novamente. A calculadora já está funcionando?


**Exercício 5**: Construa a árvore de derivação que representa a expressão 3 + 4.


**Exercício 6 [calcparser.py]**: Desenvolva código para todos as demais funções de calcparser.py. Execute o código. E agora. A calculadora funciona? 


**Exercício 7  [calcparser.py]**: Caso ainda não funcione. Insira o que falta para a calculadora funcionar. 

**Exercício 8 [dertreeparser.py]**: Faça uma nova versão do arquivo calcparser.py. Ao invés da calculadora, o objetivo agora é gerar a árvore de derivação. Realize print, em cada uma das funções de forma que seja gerada a árvore de derivação relativa a expressão aritmética passada.

