package com.luxoft.mpp.entity.model;

/**
 * Created by xXx on 6/4/2016.
 */
public class ProcessorLink {

    private Processor from;

    private Processor to;

    private  int transferDuration;

    private boolean canTransfer = true;

    public ProcessorLink() {
    }

    public ProcessorLink(Processor from, Processor to, int transferDuration) {
        this.from = from;
        this.to = to;
        this.transferDuration = transferDuration;
    }

    public ProcessorLink(Processor from, Processor to) {
        this.from = from;
        this.to = to;
        this.transferDuration = transferDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessorLink)) return false;

        ProcessorLink that = (ProcessorLink) o;

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }

    public Processor getFrom() {
        return from;
    }

    public void setFrom(Processor from) {
        this.from = from;
    }

    public Processor getTo() {
        return to;
    }

    public void setTo(Processor to) {
        this.to = to;
    }

    public int getTransferDuration() {
        return transferDuration;
    }



    public void setTransferDuration(int transferDuration) {
        this.transferDuration = transferDuration;
    }

    @Override
    public String toString() {
        return "ProcessorLink{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }

    public boolean isCanTransfer() {
        return canTransfer;
    }

    public void setCanTransfer(boolean canTransfer) {
        this.canTransfer = canTransfer;
    }

}
