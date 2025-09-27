package org.fortyK.reader;

import org.fortyK.model.Detachment;

public class DetachmentExtractor {
    private final String rawText;

    public DetachmentExtractor(String rawText)
    {
        this.rawText = rawText;
    }

    Detachment extractDetachment()
    {
        //TODO figure out what the hell to do with detachments
        return new Detachment();
    }
}
