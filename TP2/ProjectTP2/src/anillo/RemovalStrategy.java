package anillo;

public abstract class RemovalStrategy {
    public abstract Link execute(Link.ElementLink current);

    public static class LastRemoval extends RemovalStrategy {
        public Link execute(Link.ElementLink current) {
            return new Link.EmptyLink();
        }
    }

    public static class NotLastRemoval extends RemovalStrategy {
        public Link execute(Link.ElementLink current) {
            current.getPrevLink().setNext(current.getNextLink());
            current.getNextLink().setPrev(current.getPrevLink());
            return current.getNextLink();
        }
    }
}