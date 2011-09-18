/** tree.c **/
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
 * @param a : The first Node struct.
 * @param b : The second Node struct.
 * @return Greater than 0 if the first string comes after the second string, less than 0 if the
 * opposite occurs, or 0 if the two are the same.
 */
int compare_nodes(const void* a, const void* b) {
   Node* node1 = *(Node**) a;
   Node* node2 = *(Node**) b;
   return strcmp(node1->position, node2->position);
}

/**
 * Prints the value and position of a node. If the node has one or more children, their values are
 * printed as well.
 *
 * @param node : The node whose value and position are to be printed, along with those of its
 * children, if it has any.
 * @param 0 on success, -1 if node is null.
 */
int print_node(Node* node) {
   if (node == NULL) {
     printf("Node is null.\n");
     return -1;
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

   return 0;
}

/**
 * Creates a dynamic array of pointers to nodes.
 *
 * @param size : The size of the dynamic array.
 * @return The dynamic array of pointers to nodes (i.e. a pointer to an array of pointers), or null
 * if memory for the array or an element could not be allocated.
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
 * Processes all of the lines in a file and creates nodes, which are stored in an array.
 *
 * @param nodes : The array at which to store the nodes.
 * @return 0 on success, -1 if the file could not be opened or nodes is null.
 */
int process_file(Node** nodes) {
   char line[MAX_LENGTH];
   char* filename = "C:\\tree.txt";
   char* tokenizer;
   int index = 0;
   FILE *ifp;

   if (!nodes) {
     printf("Array is null.\n");
     return -1;
   }

   ifp = fopen(filename, "r");   
   if (!ifp) {
     printf("Error opening file: %s.", filename);
     return -1;
   }

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
         else if (size != 1) { /* Descendants of root node */
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

   fclose(ifp);

   return 0;
}

/**
 * Sorts an array of nodes.
 *
 * @param nodes : The array of nodes to sort.
 * @return The index at which the root node is located, or -1 if nodes is null.
 */
int sort_array(Node** nodes) {
   int index, rootIndex;

   if (!nodes) {
     printf("Array is null.\n");
     return -1;
   }

   qsort(nodes, NUM_NODES, sizeof(Node*), compare_nodes);
   for (index = 0; index < NUM_NODES; index++) {
     if (strlen(nodes[index]->position) != 0 && nodes[index]->position[0] == '-') {
       rootIndex = index;
     }
   }

   return rootIndex;
}

/**
 * Creates a binary tree given an array of nodes.
 *
 * @param nodes : The array of nodes.
 * @param rootIndex : The index at which the root node is located in the array.
 * @return 0 on success; -1 if a character other than an L, R, or - is found, nodes is null, or a
 * character could not be inserted into the tree.
 */
int create_binary_tree(Node** nodes, int rootIndex) {
   int index;

   if (!nodes) {
     printf("Array is null.\n");
     return -1;
   }

   for (index = 0; index < NUM_NODES; index++) {
     Node* currentNode = nodes[index];
     Node* parent = nodes[rootIndex];
     int pos = 0;
     char c = currentNode->position[pos];
     char isInserted = 0;

     if (strlen(currentNode->position) == 0) {
       continue;
     }

     while (c != '\0') {
       if (c == 'L' && parent->left == NULL) {
         parent->left = currentNode;
         isInserted = 1;
       }
       else if (c == 'R' && parent->right == NULL) {
         parent->right = currentNode;
         isInserted = 1;
       }
       else if (c == 'L') {
         parent = parent->left;
       }
       else if (c == 'R') {
         parent = parent->right;
       }
       else if (c != '-') { /* Except root node */
         printf("Invalid character found: %c.\n", c);
         return -1;
       }
       pos++;
       c = currentNode->position[pos];
     }

     if (!isInserted) {
       printf("Unable to insert the following node into the tree: ");
       print_node(currentNode);
       return -1;
     }
   }

   return 0;
}

/**
 * Prints all of the nodes stored in an array.
 *
 * @param nodes : The array of nodes.
 * @return 0 on success, -1 if nodes is null.
 */
int print_nodes(Node** nodes) {
   int index;

   if (!nodes) {
     printf("Array is null.\n");
     return -1;
   }

   for (index = 0; index < NUM_NODES; index++) {
     if (nodes[index] && strlen(nodes[index]->position) != 0) {
       print_node(nodes[index]);
     }
   }

   return 0;
}

/**
 * Prints the contents of a binary tree in level order using a queue.
 *
 * @param nodes : The binary tree to print.
 * @param rootIndex : The index at which the root node is located in the array.
 * @return 0 on success, -1 if memory could not be allocated for the queue or nodes is null.
 */
Node** print_binary_tree(Node** nodes, int rootIndex) {
   int index, queueIndex;
   Node** queue;

   if (!nodes) {
     printf("Array is null.\n");
     return NULL;
   }

   queue = create_array(NUM_NODES);
   if (!queue) {
     printf("Unable to allocate space for the queue.\n");
     return NULL;
   }

   printf("Level order:\n");
   queue[0] = nodes[rootIndex];
   queueIndex = 0;
   index = 0;
   while (1) {
     Node* node = queue[index];
     if (node) {
       if (node->left) {
         queueIndex++;
         queue[queueIndex] = node->left;
       }
       if (node->right) {
         queueIndex++;
         queue[queueIndex] = node->right;
       }
     }
     /* No popping necessary, just move the index forward. */
     index++;
     if (index == NUM_NODES) {
       break;
     }
   }

   return queue;
}

/**
 * Reads in a file containing a list of nodes, builds a tree with those nodes, and prints the nodes
 * in level order. I created this program to test how easy it could be to program in C using the
 * object-oriented approach (although I didn't use any function pointers in this program).
 *
 * Note that unlike the Java version of this program, only one binary tree could be created.
 *
 * @return 0 on success, -1 on failure.
 */
int main() {
   int rootIndex;
   Node** nodes;
   Node** queue;

   nodes = create_array(NUM_NODES);
   if (!nodes) {
     return -1;
   }

   if (process_file(nodes)) {
     return -1;
   }

   rootIndex = sort_array(nodes);

   if (create_binary_tree(nodes, rootIndex)) {
     return -1;
   }

   queue = print_binary_tree(nodes, rootIndex);
   if (!queue) {
     return -1;
   }

   print_nodes(queue);

   getchar();

   return 0;
}
