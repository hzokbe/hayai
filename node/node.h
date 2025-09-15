#ifndef NODE_H
#define NODE_H
typedef struct Node Node;

Node* new_node(void*);

void delete_node(Node**, void (*) (void*));

Node* get_next_node(const Node*);

Node* set_next_node(Node*, Node*);
#endif
