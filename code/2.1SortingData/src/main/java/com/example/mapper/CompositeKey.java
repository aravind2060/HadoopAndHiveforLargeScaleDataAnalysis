package com.example.mapper;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CompositeKey implements WritableComparable<CompositeKey> {
    private String stationId;
    private String date;
    private String elementType;

    // Default constructor for deserialization
    public CompositeKey() {
    }

    public CompositeKey(String stationId, String date, String elementType) {
        this.stationId = stationId;
        this.date = date;
        this.elementType = elementType;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        WritableUtils.writeString(out, stationId);
        WritableUtils.writeString(out, date);
        WritableUtils.writeString(out, elementType);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        stationId = WritableUtils.readString(in);
        date = WritableUtils.readString(in);
        elementType = WritableUtils.readString(in);
    }

    @Override
    public int compareTo(CompositeKey o) {
        int result = stationId.compareTo(o.stationId);
        if (result == 0) {
            result = date.compareTo(o.date);
            if (result == 0) {
                result = elementType.compareTo(o.elementType);
            }
        }
        return result;
    }

    // Getters, setters, and hashCode() and equals() methods omitted for brevity
}
