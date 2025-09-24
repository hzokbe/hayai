#include "stack.h"

#include "../node/node.h"

#include <stdlib.h>

typedef struct Stack
{
  Node* top;
} Stack;

Stack* new_stack(void)
{
  Stack* s = malloc(sizeof(Stack));

  if (!s)
  {
    return NULL;
  }

  s->top = NULL;

  return s;
}

void delete_stack(Stack** stack_ptr, void (*destructor) (void*))
{
  Stack* s = *stack_ptr;

  if (s)
  {
    Node* top = s->top;

    while (top)
    {
      Node* next_node = get_next_node(top);

      destructor(get_value(top));

      free(top);

      top = next_node;
    }
  }

  free(*stack_ptr);

  *stack_ptr = NULL;
}

void push(Stack* stack, void* value)
{
  if (!stack)
  {
    return;
  }

  Node* top = new_node(value);

  set_next_node(top, stack->top);

  stack->top = top;
}

void* peek(Stack* stack)
{
  if (!stack)
  {
    return NULL;
  }

  return get_value(stack->top);
}
