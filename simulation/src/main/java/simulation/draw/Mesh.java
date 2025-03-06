package simulation.draw;

public class Mesh {
    private int vaoId;
    private int vertices;

    public Mesh(int vaoId, int vertices){
        this.vaoId=vaoId;
        this.vertices=vertices;
    }

    public int getVaoId(){
        return vaoId;
    }

    public int getVertices(){
        return vertices;
    }
}
