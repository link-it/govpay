import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

export interface ConfermaForzaOperazioneData {
  titolo: string;
  messaggio: string;
}

@Component({
  selector: 'app-conferma-forza-operazione-dialog',
  templateUrl: './conferma-forza-operazione-dialog.component.html',
  styleUrls: ['./conferma-forza-operazione-dialog.component.scss']
})
export class ConfermaForzaOperazioneDialogComponent {
  forzaOperazione: boolean = true;

  constructor(
    public dialogRef: MatDialogRef<ConfermaForzaOperazioneDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConfermaForzaOperazioneData
  ) {}

  onAnnulla(): void {
    this.dialogRef.close(null);
  }

  onConferma(): void {
    this.dialogRef.close(this.forzaOperazione);
  }
}
