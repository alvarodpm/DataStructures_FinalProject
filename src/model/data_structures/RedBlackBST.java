package model.data_structures;

import java.util.Iterator;
/**
 * Estructura tomada de  la sección 3.3 Balanced Search Trees del libro guía Algorithms de Sedgewick y Wayne.
 */
public class RedBlackBST<K extends Comparable<K>, V> implements IRedBlackBST<K, V>{

	/**
	 * Constantes que indican el color del nodo
	 */
	private static final boolean RED = true; 
	private static final boolean BLACK = false;


	/**
	 * Clase interna que representa un nodo del árbol
	 */
	private class TreeNode<K extends Comparable<K>, V> 
	{

		//Atributos

		K key;                  // key
		V val;                  // associated data 
		TreeNode left, right;   // subtrees
		int N;                  // # nodes in this subtree 
		boolean color;          // color of link from parent to this node

		public TreeNode(K key, V val, int N, boolean color)
		{
			this.key   = key;     
			this.val   = val;     
			this.N     = N;     
			this.color = color;
		}
		
		public boolean esHoja()
		{
			if(left == null && right == null)
				return true;
			else
				return false;
		}
	}



	//Atributos

	private TreeNode<K, V> root;

	public RedBlackBST() 
	{
		root = null;
	}

	//Métodos

	@Override
	public int size() 
	{
		return size(root);
	}

	@Override
	public boolean isEmpty() {
		return root == null;
	}

	@Override
	public V get(K key) {
		return get(root, key);
	}

	@Override
	public int getHeight(K key) 
	{
		return getHeight(key, root, 0);
	}

	@Override
	public boolean contains(K key) {
		return get(key) != null;
	}

	@Override
	public void put(K key, V val) throws Exception
	{
		root = put(root, key, val);     
		root.color = BLACK;
	}

	@Override
	public int height() {
		return height(root);
	}

	@Override
	public K minKey() 
	{
		return minKey(root);
	}

	@Override
	public K maxKey() {
		return maxKey(root);
	}
	
	@Override
	public V minValue()
	{
		return minValue(root);
	}
	
	@Override
	public V maxValue()
	{
		return maxValue(root);
	}

	@Override
	public boolean check() {
		return check(root) != -1;
	}
	
	@Override
	public Iterator<K> keys(){
		return toList(root, new ListaEncadenada<K>()).iterator();
	}

	@Override
	public Iterator<V> valuesInRange(K init, K end) {
		return valuesInRange(init, end, root, new ListaEncadenada<V>()).iterator();
	}
	
	public Iterator<V> nValuesInRange(K init, K end, int cant)
	{
		return nValuesInRange(init, end, root, new ListaEncadenada<V>(), cant).iterator();
	}

	@Override
	public Iterator<K> keysInRange(K init, K end) {
		return keysInRange(init, end, root, new ListaEncadenada<K>()).iterator();
	}

	private boolean isRed(TreeNode<K, V> x) 
	{ 
		if (x == null) 
			return false;  
		return x.color == RED; 
	}

	private TreeNode<K, V> rotateLeft(TreeNode<K, V> h) 
	{   
		TreeNode<K, V> x = h.right;   
		h.right = x.left;   
		x.left = h;   
		x.color = h.color;   
		h.color = RED;   
		x.N = h.N;   
		h.N = 1 + size(h.left) + size(h.right);   
		return x; 
	}

	private TreeNode<K, V> rotateRight(TreeNode<K, V> h) 
	{   
		TreeNode<K, V> x = h.left;  
		h.left = x.right;   
		x.right = h;   
		x.color = h.color;   
		h.color = RED;   
		x.N = h.N;   
		h.N = 1 + size(h.left) + size(h.right);   
		return x; 
	}

	private void flipColors(TreeNode<K, V> h)
	{
		h.color = RED;   
		h.left.color = BLACK;   
		h.right.color = BLACK;
	}

	private TreeNode<K, V> put(TreeNode<K, V> h, K key, V val) throws Exception
	{
		if(key == null)
			throw new Exception("El valor de la llave no puede ser null");
		
		if(val == null)
			throw new Exception("El valor del elemento no puede ser null");
		
		if (h == null)  // Do standard insert, with red link to parent.         
			return new TreeNode<K, V>(key, val, 1, RED);

		int cmp = key.compareTo(h.key);  

		if(cmp < 0) 
			h.left = put(h.left,  key, val);      
		else if(cmp > 0) 
			h.right = put(h.right, key, val);      
		else h.val = val;

		if (isRed(h.right) && !isRed(h.left))    
			h = rotateLeft(h);      
		if (isRed(h.left) && isRed(h.left.left)) 
			h = rotateRight(h);      
		if (isRed(h.left) && isRed(h.right))     
			flipColors(h);

		h.N = size(h.left) + size(h.right) + 1;      
		return h;
	}

	private V get(TreeNode<K, V> x, K key)
	{
		// Return value associated with key in the subtree rooted at x;   
		// return null if key not present in subtree rooted at x.

		if (x == null) return null;   
		int cmp = key.compareTo(x.key);   
		if(cmp < 0) 
			return (V) get(x.left, key);   
		else if (cmp > 0) 
			return (V) get(x.right, key);   
		else return x.val;
	}

	private int size(TreeNode<K, V> x)   
	{      
		if (x == null) return 0;      
		else           
			return x.N;   
	}

	private int height(TreeNode<K, V> node)
	{
		if(node == null)
			return -1;
		int leftHeight = height(node.left);
		int rightHeight = height(node.right);
		
		return Math.max(leftHeight, rightHeight) + 1;
	}
	
	/**
	 * Basado en https://www.geeksforgeeks.org/get-level-of-a-node-in-a-binary-tree/
	 */
	private int getHeight(K key, TreeNode<K, V> node, int level)
	{
		if (node == null) 
            return -1;
		
		if (node.key == key) 
            return level; 
		
		int downlevel = getHeight(key, node.left, level+1);
		if (downlevel != -1) 
            return downlevel;
		
		downlevel = getHeight(key, node.right, level+1);
		
		return downlevel;
	}
	
	/**
	 * Tomado de https://www.geeksforgeeks.org/find-the-minimum-element-in-a-binary-search-tree/
	 */
	private K minKey(TreeNode<K, V> node)
	{
		if(node == null)
			return null;

		while (node.left != null) { 
			node = node.left; 
		} 
		return (node.key); 
	}
	
	private V minValue(TreeNode<K, V> node)
	{
		if(node == null)
			return null;

		while (node.left != null) { 
			node = node.left; 
		} 
		return (node.val); 
	}
	
	/**
	 * Tomado de https://www.geeksforgeeks.org/find-the-minimum-element-in-a-binary-search-tree/
	 */
	private K maxKey(TreeNode<K, V> node)
	{
		if(node == null)
			return null;

		while (node.right != null) { 
			node = node.right; 
		} 
		return (node.key); 
	}
	
	private V maxValue(TreeNode<K, V> node)
	{
		if(node == null)
			return null;

		while (node.right != null) { 
			node = node.right; 
		} 
		return (node.val); 
	}
	
	
	private int check(TreeNode<K, V> node)
	{
		if (node == null)
	        return 0; 
		
		//Verificar si el nodo padre es mayor que su hijo izquierdo y menor que el derecho
		if(node.left != null && node.key.compareTo((K) node.left.key) < 0)
			return -1;
		if(node.right != null && node.key.compareTo((K) node.right.key) > 0)
			return -1;
		
		//Verificar que el hijo derecho no tiene enlace rojo
		if(node.right != null && node.right.color == RED)
			return -1;
		
		//Verificar que no hayan dos enlaces rojos consecutivos a la izqiuerda
		if(node.left != null && node.left.left != null && node.left.color == RED && node.left.left.color == RED)
			return -1;
		
		//Verificar que todas las ramas tienen el mismo número de enlaces negros
		//Tomado de https://stackoverflow.com/questions/27731072/check-whether-a-tree-satisfies-the-black-height-property-of-red-black-tree
		int leftHeight = check(node.left);
	    int rightHeight = check(node.right);
	    
	    if (leftHeight == -1 || rightHeight == -1 || leftHeight != rightHeight)
	        return -1; 
	    else
	    {
	    	int add = node.color == BLACK ? 1 : 0;
	    	return leftHeight + add;
	    }
	}
	
	private ListaEncadenada<K> toList(TreeNode<K, V> node, ListaEncadenada<K> lista)
	{		
		if(node == null)
			return lista;
		
		
		if(node.right != null)
			lista = toList(node.right, lista);
		
		lista.addFirst(node.key);				
				
		if(node.left != null)
			lista = toList(node.left, lista);
		
		return lista;
	}
	
	/**
	 * Basado en https://www.geeksforgeeks.org/print-bst-keys-in-the-given-range/
	 */
	private ListaEncadenada<K> keysInRange(K init, K end, TreeNode<K, V> node, ListaEncadenada<K> lista)
	{
		if(node == null)
			return lista;
		
		if(end.compareTo(node.key) > 0)
			keysInRange(init, end, node.right, lista);
		
		if(init.compareTo(node.key) <= 0 && end.compareTo(node.key) >= 0)
			lista.addFirst(node.key);
		
		if(init.compareTo(node.key) < 0)
			keysInRange(init, end, node.left, lista);
		
		return lista;
	}
	
	private ListaEncadenada<V> nValuesInRange(K init, K end, TreeNode<K, V> node, ListaEncadenada<V> lista, int cantidad)
	{
		if(cantidad <= 0)
			return lista;
		
		if(node == null)
			return lista;
		
		if(end.compareTo(node.key) > 0 && lista.size() < cantidad)
			nValuesInRange(init, end, node.right, lista, cantidad);
		
		if(init.compareTo(node.key) <= 0 && end.compareTo(node.key) >= 0 && lista.size() < cantidad)
			lista.addFirst(node.val);
		
		if(init.compareTo(node.key) < 0 && lista.size() < cantidad)
			nValuesInRange(init, end, node.left, lista, cantidad);
		
		return lista;
	}
	
	/**
	 * Basado en https://www.geeksforgeeks.org/print-bst-keys-in-the-given-range/
	 */
	private ListaEncadenada<V> valuesInRange(K init, K end, TreeNode<K, V> node, ListaEncadenada<V> lista)
	{
		if(node == null)
			return lista;
		
		if(end.compareTo(node.key) > 0)
			valuesInRange(init, end, node.right, lista);
		
		if(init.compareTo(node.key) <= 0 && end.compareTo(node.key) >= 0)
			lista.addFirst(node.val);
		
		if(init.compareTo(node.key) < 0)
			valuesInRange(init, end, node.left, lista);
		
		return lista;
	}
	
	public double darAlturaPromedioHojas()
	{	
		if(isEmpty())
			return -1;
		
		ListaEncadenada<K> keysHojas = keysHojas(root, new ListaEncadenada<K>());
		
		double cantidadHojas = keysHojas.size();
		
		double alturasAcumuladas = 0;
		
		Iterator<K> iterador = keysHojas.iterator();
		
		while(iterador.hasNext())
		{
			alturasAcumuladas += getHeight(iterador.next());
		}
		
		return alturasAcumuladas/cantidadHojas;
	}
	
	public ListaEncadenada<K> keysHojas(TreeNode<K, V> node, ListaEncadenada<K> lista)
	{
		if(node.esHoja())
			lista.addFirst(node.key);
		else
		{
			if(node.right != null)
				keysHojas(node.right, lista);
			if(node.left != null)
				keysHojas(node.left, lista);
		}
		
		return lista;
	}
}
