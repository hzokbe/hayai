#include "node.h"

#include <stdlib.h>

typedef struct Node
{
  void* value;

  Node* next_node;
} Node;

Node* new_node(void *value)
{
  Node* n = malloc(sizeof(Node));

  if (!n)
  {
    return NULL;
  }

  n->value = value;

  n->next_node = NULL;

  return n;
}

void delete_node(Node** node_ptr, void (*destructor)  (void *))
{
  if (destructor)
  {
    destructor((*node_ptr)->value);
  }

  free(*node_ptr);

  *node_ptr = NULL;
}

Node* get_next_node(const Node* node)
{
  return node ? node->next_node : NULL;
}

Node* set_next_node(Node* node, Node* next_node)
{
  if (!node)
  {
    return NULL;
  }

  Node* temp = node->next_node;

  node->next_node = next_node;

  return temp;
}

void* get_value(const Node* node)
{
  if (node)
  {
    return node->value;
  }

  return NULL;
}

void* set_value(Node* node, void* value)
{
  if (!node)
  {
    return NULL;
  }

  void* temp = node->value;

  node->value = value;

  return temp;
}
