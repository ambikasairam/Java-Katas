#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_LENGTH 100
#define NUM_NODES  100

/** A node on a binary tree. */
typedef struct node {
   int value;
   char position[20];
   struct node* left;
   struct node* right;
} Node;

/**
 * Given two Node structs, compares the two using the strings found inside them.
 *
 * @param a The first Node struct.
 * @param b The second Node struct.
 * @return Greater than 0 if the first string comes after the second string, less than 0 if the
 * opposite occurs, or 0 if the two are the same.
 */
int compare_nodes(const void* a, const void* b) {
   Node* node1 = *(Node**) a;
   Node* node2 = *(Node**) b;
   return strcmp(node1->position, node2->position);
}

/**
 * Prints the value and position of a node.
 *
 * @param The node whose value and position are to be printed.
 */
void print_node(Node* node) {
   if (node == NULL) {
     printf("Node is null.\n");
     return;
   }

   if (node->left == NULL && node->right == NULL) {
     printf("Node=[value=%d,position=%s]\n", node->value, node->position);
   }
   else if (node->left == NULL) {
     printf("Node=[value=%d,position=%s,right=%d]\n", node->value, node->position,
       node->right->value);
   }
   else if (node->right == NULL) {
     printf("Node=[value=%d,position=%s,left=%d]\n", node->value, node->position,
       node->left->value);
   }
   else {
     printf("Node=[value=%d,position=%s,left=%d,right=%d]\n", node->value, node->position,
       node->left->value, node->right->value);   
   }
}

/**
 * Creates a dynamic array of pointers to nodes.
 *
 * @param The size of the dynamic array.
 * @return The dynamic array of pointers to nodes (i.e. a pointer to an array of pointers).
 */
Node** create_array(int size) {
   int index;
   Node** nodes = (Node**) calloc(size, sizeof(Node*));
   if (!nodes) {
     printf("Unable to allocate 2D array of nodes.\n");
     return NULL;
   }
   for (index = 0; index < size; index++) {
     nodes[index] = (Node*) calloc(1, sizeof(Node));
     if (!nodes[index]) {
       printf("Unable to allocate a node at index position %d.\n", index);
       return NULL;
     }
     nodes[index]->left = NULL;
     nodes[index]->right = NULL;
   }
   return nodes;
}

/**
 * Reads in a file containing a list of nodes, builds a tree with those nodes, and prints the nodes
 * in level order.
 */
int main() {
   FILE* ifp;
   char line[MAX_LENGTH];
   char* tokenizer;
   char* filename = "tree.txt";
   int counter, index, numNodes, queueIndex, rootIndex;
   Node** queue;

   Node** nodes = create_array(NUM_NODES);

   ifp = fopen(filename, "r");   
   if (!ifp) {
     printf("Error opening file: %s.", filename);
     return -1;
   }

   /* Add all of the nodes to the array. */
   index = 0;
   while (!feof(ifp)) {
     if (fgets(line, MAX_LENGTH, ifp)) {
       tokenizer = strtok(line, "() ");
       while (tokenizer) {
         int size = strlen(tokenizer);
         if (tokenizer[size - 1] == ',') { /* Root node */
           tokenizer[size - 1] = '\0';
           strcpy(nodes[index]->position, "-");
           nodes[index]->value = atoi(tokenizer);
           index++;
         }
         else if (size != 1) {
           char* ptr = strstr(tokenizer, ",");
           strcpy(nodes[index]->position, ptr + 1);
           *ptr = '\0';
           nodes[index]->value = atoi(tokenizer);
           index++;
         }
         tokenizer = strtok(NULL, "() ");
       }
     }
   }
   numNodes = index;

   /* Sort the array of nodes. */
   qsort(nodes, NUM_NODES, sizeof(Node*), compare_nodes);
   for (index = 0; index < NUM_NODES; index++) {
     if (strlen(nodes[index]->position) != 0 && nodes[index]->position[0] == '-') {
       rootIndex = index;
     }
   }

   /* Create the binary tree. */
   for (index = 0; index < NUM_NODES; index++) {
     Node* currentNode = nodes[index];
     Node* parent = nodes[rootIndex];
     int pos = 0;
     char c = currentNode->position[pos];

     if (strlen(currentNode->position) == 0) {
       continue;
     }

     while (c != '\0') {
       if (c == 'L' && parent->left == NULL) {
         parent->left = currentNode;
       }
       else if (c == 'R' && parent->right == NULL) {
         parent->right = currentNode;
       }
       else if (c == 'L') {
         parent = parent->left;
       }
       else if (c == 'R') {
         parent = parent->right;
       }
       pos++;
       c = currentNode->position[pos];
     }
   }

   /* Print the binary tree. */
   for (index = 0; index < NUM_NODES; index++) {
     if (strlen(nodes[index]->position) != 0) {
       print_node(nodes[index]);
     }
   }

   queue = create_array(NUM_NODES);
   if (!queue) {
     printf("Unable to allocate space for the queue to store the nodes.\n");
     return -1;
   }

   for (index = 0, counter = 0, queueIndex = 0; index < NUM_NODES; index++) {
     if (strlen(nodes[index]->position) != 0) {
       queue[queueIndex] = nodes[index];
       queue[queueIndex + 1] = nodes[index]->left;
       queue[queueIndex + 2] = nodes[index]->right;
       queueIndex += 3;
       counter++;
     }
     if (counter == numNodes) {
       break;
     }
   }

   fclose(ifp);

   return 0;
}
