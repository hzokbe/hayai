#ifndef STACK_H
#define STACK_H
typedef struct Stack Stack;

Stack* new_stack(void);

void delete_stack(Stack**, void (*) (void*));

void push(Stack*, void*);

void* peek(Stack*);

void* pop(Stack*);
#endif
