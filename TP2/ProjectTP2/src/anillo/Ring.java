package anillo;

public class Ring {

    private Node current;

    private static class Node {
        Object value;
        Node next;

        Node(Object value) {
            this.value = value;
            this.next = this; // apunta a si mismo (caso: un solo nodo en anillo)
        }
    }

    public Ring() {  // Crear ring vacío
        this.current = null;
    }

    public Ring next() {
        if (current != null) {current = current.next;}
        if (current == null) {throw new IllegalStateException("Ring is empty");} // esta excepción es para que pase test00NextOnEmpty
        return this;
    }

    public Object current() {
        if (current == null) {return null;}
        return current.value;
    }

    public Ring add( Object cargo ) {
        if (current == null) {
            current = new Node(cargo);
        }
        else {
            Node newNode = new Node(cargo);
            newNode.next = current.next;
            current.next = newNode;
            current = newNode;
        }
        return this;
    }

    public Ring remove() {
        if (current == null) {return this;}
        if (current.next == current) {  // caso: solo un nodo
            current = null;
        }
        else {
            Node prevNode = current;
            while (prevNode.next != current) {  // da toda la vuelta al anillo hasta encontrar al anterior (no hay prev, solo next)
                prevNode = prevNode.next;  // queda definido el prevNode correctamente
            }
            prevNode.next = current.next;
            current = current.next;
        }
        return this;
    }
}
