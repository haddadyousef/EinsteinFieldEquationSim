package simulation.draw;

import org.joml.Vector3f;

public class Grid extends Object{

    private Mesh mesh;
    private float[]vertices;
    private float[]colors;
    private int[]indices;

    public Grid(){
        super(0);
    }


    public void createGrid(int row, int col){
        float[]vertices = new float[3*row*col];
        int[]indices = new int[4*row*col-2*row-2*col];
        float[]colors =new float[3*row*col];

        float x=-150f;
        float y=0f;
        float z=100f;

        int index=0;
        int index1=0;

        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                vertices[index]=x;
                colors[index]=0f;
                index++;
                vertices[index]=y;
                colors[index]=0f;
                index++;
                vertices[index]=z;
                colors[index]=1f;
                index++;
                x+=2.5f;
                if(j<col-1){
                    indices[index1]=col*i+j;
                    index1++;
                    indices[index1]=col*i+j+1;
                    index1++;
                }
               if(i<row-1){
                indices[index1]=col*i+j;
                index1++;
                indices[index1]=col*(i+1)+j;
                index1++;
               }
                
            }
            z+=-2.5f;
            x=-150f;
        }

        this.mesh = MeshGen.createMesh(vertices, colors, indices);
        this.vertices = vertices;
        this.colors = colors;
        this.indices = indices;
    }

    public Mesh getMesh(){
        return mesh;
    }
    
     public Vector3f getRotation(){
        return new Vector3f(0,0,0);
    }


    public Vector3f getPosition(){
        return new Vector3f(0,0,0);
    }

    public float[]getVertices(){
        return vertices;
    }

    public void setVertices(float[]vertices){
        this.vertices = vertices;
        this.mesh = MeshGen.createMesh(vertices, colors, indices);
    }
}
