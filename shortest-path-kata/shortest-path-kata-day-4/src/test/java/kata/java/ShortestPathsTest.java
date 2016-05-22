package kata.java;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class ShortestPathsTest {

    private EdgeWeightedDigraph digraph;

    @Before
    public void setUp() throws Exception {
        digraph = EdgeWeightedDigraph.create();
    }

    @Test
    public void itShouldCreateAShortestPath() throws Exception {
        digraph.addEdge(DirectedEdge.create(1, 2, 1.0));

        ShortestPaths.create(digraph, 1);
    }

    @Test(expected = RuntimeException.class)
    public void itShouldNotCreateAShortestPathFromAnEmptyDigraph() throws Exception {
        ShortestPaths.create(digraph, 1);
    }

    @Test
    public void itShouldContainTwoEdgesInPath() throws Exception {
        digraph.addEdge(DirectedEdge.create(1, 2, 1.0));
        digraph.addEdge(DirectedEdge.create(2, 3, 1.0));
        digraph.addEdge(DirectedEdge.create(1, 3, 4.0));

        ShortestPaths paths = ShortestPaths.create(digraph, 1);

        assertThat(paths.pathTo(3), contains(DirectedEdge.create(1, 2, 1.0), DirectedEdge.create(2, 3, 1.0)));
    }

    @Test
    public void itShouldContainThreeEdgesInPath() throws Exception {
        digraph.addEdge(DirectedEdge.create(1, 2, 1.0));
        digraph.addEdge(DirectedEdge.create(2, 3, 1.0));
        digraph.addEdge(DirectedEdge.create(3, 4, 1.0));
        digraph.addEdge(DirectedEdge.create(1, 4, 5.0));

        ShortestPaths paths = ShortestPaths.create(digraph, 1);

        assertThat(paths.pathTo(4), contains(DirectedEdge.create(1, 2, 1.0), DirectedEdge.create(2, 3, 1.0), DirectedEdge.create(3, 4, 1.0)));
    }
}
