package com.example.further;

import android.location.Location;

public class LocationNode<Location> {
    private LocationNode<Location> next;
    private LocationNode<Location> prev;
    private Location data;
    private int length;

    public LocationNode(Location _data){
        this.data = _data;
        this.next = null;
        this.length = 0;
    }
    public Location getData(){
        return (Location) this.data;
    }

    public LocationNode getNext(){
        return  this.next;
    }

    public int getLength(){
        return this.length;
    }
    public LocationNode<Location> getPrev(){ return this.prev;}

    public void setNext(LocationNode<Location> _next){
        this.next = _next;
    }

    private void setLength(int _length){
        this.length = _length;
    }

    private void setPrev(LocationNode<Location> _prev){
        this.prev = _prev;
    }

    public LocationNode<Location> addNode(Location _data){
        if (this == null){
            return null;
        }

        if (this.next == null){
            this.setNext(new LocationNode<Location>(_data));
            this.getNext().setPrev(this);
            return next;
        }
        else {
            this.setLength(this.getLength() + 1);
            return this.next.addNode(_data);
        }
    }
}
