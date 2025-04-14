package anillo;

import java.util.Stack;

public class Ring {

    private final Stack<RemovalStrategy> removalStrats = new Stack<>();
    private Link currentLink = new Link.EmptyLink();

    public Ring add(Object cargo) {
        currentLink = currentLink.addPrevLink(cargo, removalStrats);
        return this;
    }

    public Ring next() {
        currentLink = currentLink.next();
        return this;
    }

    public Object current() {
        return currentLink.getCargo();
    }

    public Ring remove() {
        currentLink = currentLink.removeMyself(removalStrats);
        return this;
    }
}