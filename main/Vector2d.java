package main;

import java.util.Random;

public class Vector2d {
    public final int x;
    public final int y;
    Random generator = new Random();

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return ("(" + this.x + "," + this.y + ")");
    }

    public boolean precedes(Vector2d other){
        return (this.x <= other.x && this.y <= other.y);
    }

    public boolean follows(Vector2d other){
        return (this.x >= other.x && this.y >= other.y);
    }

    public Vector2d upperRight(Vector2d other){
        int x= this.x;
        int y=this.y;
        if(other.x > this.x) x= other.x;
        if(other.y > this.y) y= other.y;
        Vector2d result=new Vector2d(x , y);
        return result;
    }

    public Vector2d lowerLeft(Vector2d other){
        int x= this.x, y=this.y;
        if(other.x < this.x) x= other.x;
        if(other.y < this.y) y= other.y;
        Vector2d result=new Vector2d(x , y);
        return result;
    }

    public Vector2d add(Vector2d other){
        int x= this.x+other.x, y=this.y+other.y;
        Vector2d result=new Vector2d(x , y);
        return result;
    }

    public Vector2d subtract(Vector2d other){
        int x= this.x - other.x, y=this.y - other.y;
        Vector2d result=new Vector2d(x , y);
        return result;
    }

    public Vector2d opposite(){
        Vector2d that = new Vector2d( - this.x, - this.y);
        return that;
    }

    public boolean equals(Object other){
        //if(this == other) return true;
        if(!(other instanceof Vector2d)) return false;
        Vector2d that = (Vector2d) other;
        return(this.x == that.x && this.y == that.y);
    }

    public Vector2d mod(Vector2d other){
        return new Vector2d((this.x + other.x + 1) % (other.x + 1), (this.y + other.y + 1) % (other.y + 1));
    }

    public Vector2d random(){
        return new Vector2d(generator.nextInt(this.x + 1) , generator.nextInt(this.y + 1));
    }

    @Override
    public int hashCode() {
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }

}
