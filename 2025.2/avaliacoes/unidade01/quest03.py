# 3. Adotando a gramática da questão anterior, considere as seguintes regras: (2,0)
# num     = Começa com 0 ou 1 ou 2, seguido por quaisquer quantidades de 0 ou 1 e 2, finalizado com 3.
# ID      = Começa com o prefixo ID_ seguido por qualquer letra ou númeor. 
# Os demais tokes são reconhecidos como eles ocorrem na gramática
# Elabore código PLY que faça o reconhecimento do léxico dessa linguagem. 


import ply.lex as lex

# List of token names.   This is always required
tokens = (
   'f',
   'c',
   'hyb',
   'jyb',
   'FUNC',
   'ID',
   'LPAR',
   'RPAR',
   'PV',
   'IGUAL',
   'num',
   'V',

)
t_ignore  = ' \t'
t_f = r'f'
t_c = r'c'
t_hyb = 'hyb'
t_jyb = 'jyb'
t_FUNC = 'FUNC'
t_ID = r"ID\_[a-zA-Z0-9]*"
t_LPAR = r'\('
t_RPAR = r'\)'
t_PV = ';'
t_V = ','
t_IGUAL = '='
t_num = "[012](0|12)*3"
t_V = ','

lexer = lex.lex()

# Test it out
data = '''f c hyb jyb FUNC ID_lu ( ) ; , = 1123 003 200123'''

# Give the lexer some input
lexer.input(data)

while True:
    tok = lexer.token()
    if not tok:
        break      # No more input
    print(tok)
