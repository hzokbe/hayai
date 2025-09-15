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

