package com.example.further;

import android.location.Location;

public class LocationNode<Location> {
    private LocationNode<Location> next;
    private Location data;
    private int length;

    public LocationNode(Location _data){
        this.data = _data;
        this.next = null;
        this.length = 0;
    }
    public Location getData(){
        return this.data;
    }

    public Location getNext(){
        return (Location) this.next;
    }

    public int getLength(){
        return this.length;
    }

    public void setNext(LocationNode<Location> _next){
        this.next = _next;
    }

    private void setLength(int _length){
        this.length = _length;
    }

    public void addNode(Location _data){
        if (this.next == null){
            this.setNext(new LocationNode<Location>(_data));
        }
        else {
            this.next.addNode(_data);
            this.setLength(this.getLength() + 1);
        }
    }
}
