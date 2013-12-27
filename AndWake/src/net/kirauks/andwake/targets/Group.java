package net.kirauks.andwake.targets;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Group implements Parcelable {
    private long id;
    private String name = "";
    private final ArrayList<Computer> children;

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel source) {
            Group g = new Group();
            g.setId(source.readLong());
            g.setName(source.readString());
            source.readTypedList(g.getChildren(), Computer.CREATOR);
            return g;
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public Group() {
        this.children = new ArrayList<Computer>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Group other = (Group) obj;
        if (this.children == null) {
            if (other.children != null) {
                return false;
            }
        }
        else if (!this.children.equals(other.children)) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public List<Computer> getChildren() {
        return this.children;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.children == null) ? 0 : this.children.hashCode());
        result = (prime * result) + (int) (this.id ^ (this.id >>> 32));
        result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.children);
    }
}
