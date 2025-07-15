
def foo(n): 
   if n > 0:
      bar(n - 1)

foo(5)

def bar(n):
   if n > 0:
      foo(n - 1)




print("end")