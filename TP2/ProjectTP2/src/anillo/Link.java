package anillo;

import java.util.Stack;

public abstract class Link {
    public abstract Link addPrevLink(Object cargo, Stack<RemovalStrategy> removalStrats);
    public abstract Link next();
    public abstract Object getCargo();
    public abstract Link removeMyself(Stack<RemovalStrategy> removalStrats);

    public static class EmptyLink extends Link {

        public Link addPrevLink(Object cargo, Stack<RemovalStrategy> removalStrats) {
            ElementLink newLink = new ElementLink(cargo);
            newLink.setNext(newLink);
            newLink.setPrev(newLink);
            removalStrats.push(new RemovalStrategy.LastRemoval());
            return newLink;
        }

        public Link next() {
            throw new IllegalStateException("Ring is empty");
        }

        public Object getCargo() {
            throw new IllegalStateException("Ring is empty");
        }

        public Link removeMyself(Stack<RemovalStrategy> removalStrats) {
            return this;
        }
    }

    public static class ElementLink extends Link {
        private final Object cargo;
        private ElementLink next;
        private ElementLink prev;

        public ElementLink(Object value) {
            this.cargo = value;
        }

        public ElementLink addPrevLink(Object cargo, Stack<RemovalStrategy> removalStrats) {
            ElementLink newLink = new ElementLink(cargo);
            newLink.setNext(this);
            newLink.setPrev(prev);
            prev.setNext(newLink);
            this.setPrev(newLink);
            removalStrats.push(new RemovalStrategy.NotLastRemoval());
            return newLink;
        }

        public Link next() {
            return next;
        }

        public Object getCargo() {
            return cargo;
        }

        public Link removeMyself(Stack<RemovalStrategy> removalStrats) {
            RemovalStrategy strat = removalStrats.pop();
            return strat.execute(this);
        }

        public ElementLink getNextLink() {
            return next;
        }

        public ElementLink getPrevLink() {
            return prev;
        }

        public void setNext(ElementLink next) {
            this.next = next;
        }

        public void setPrev(ElementLink prev) {
            this.prev = prev;
        }
    }
}